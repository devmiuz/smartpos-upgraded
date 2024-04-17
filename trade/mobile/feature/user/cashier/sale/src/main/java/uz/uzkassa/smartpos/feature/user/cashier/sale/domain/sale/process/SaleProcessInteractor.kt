package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sale.process

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.JsonDecodingException
import uz.uzkassa.smartpos.core.data.manager.printer.exception.PrinterException
import uz.uzkassa.smartpos.core.data.source.resource.marking.model.ProductMarking
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.core.utils.math.sum
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.receipt.held.ReceiptHeldBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.exception.SamModuleBrokenException
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.exception.SynchronizationException
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.process.SaleProcessDetails
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.save.ReceiptSaleSaveRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.SaleInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.product.marking.ProductMarkingInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.receipt.ReceiptSaveInteractor
import java.math.BigDecimal
import javax.inject.Inject

internal class SaleProcessInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val receiptSaveInteractor: ReceiptSaveInteractor,
    private val receiptHeldBroadcastChannel: ReceiptHeldBroadcastChannel,
    private val receiptSaleSaveRepository: ReceiptSaleSaveRepository,
    private val saleInteractor: SaleInteractor,
    private val productMarkingInteractor: ProductMarkingInteractor
) {
    private val actualAmount: BigDecimal
        get() {
            val amount: BigDecimal = saleInteractor.getTotalCost()
            return saleInteractor.getSaleDiscount()
                ?.let { amount - it.getOrCalculateDiscountAmount } ?: amount
        }

    private val changeAmount: BigDecimal
        get() = (saleInteractor.getReceiptPayments().map { it.amount }.sum() - actualAmount)
            .let { if (it < BigDecimal.ZERO) BigDecimal.ZERO else it }

    fun getSaleProcessDetails(): SaleProcessDetails =
        SaleProcessDetails(actualAmount, changeAmount)

    var creditAdvanceHolder: CreditAdvanceHolder? = receiptSaveInteractor.creditAdvanceHolder

    @FlowPreview
    fun createReceipt(): Flow<Result<Unit>> {
        return receiptSaleSaveRepository
            .createReceipt(receiptSaveInteractor.getParams(ReceiptStatus.PAID))
            .catch {
                if (it is PrinterException) {
                    saleInteractor.setReceiptCreated(true)
                    throw it
                }
                if (it is JsonDecodingException) throw SynchronizationException()
                if (it is SamModuleBrokenException) throw SamModuleBrokenException()
                else throw it
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun createCreditAdvanceReceipt(): Flow<Result<Unit>> {
        val creditAdvanceHolder = receiptSaveInteractor.creditAdvanceHolder
        if (creditAdvanceHolder != null) {

            val saleSaveParams = receiptSaveInteractor.getParams(creditAdvanceHolder.status)

            var totalCash: BigDecimal = BigDecimal.ZERO
            var totalCard: BigDecimal = BigDecimal.ZERO

            val totalPaid = creditAdvanceHolder.paymentAmount

            val receiptPayments = mutableListOf<ReceiptPayment>()

            saleSaveParams.fiscalReceipt?.let {
                when {
                    it.totalCash > BigDecimal.ZERO -> {
                        totalCash = totalPaid
                        receiptPayments.add(ReceiptPayment(totalPaid, ReceiptPayment.Type.CASH))
                    }
                    it.totalCard > BigDecimal.ZERO -> {
                        totalCard = totalPaid
                        receiptPayments.add(ReceiptPayment(totalPaid, ReceiptPayment.Type.CARD))
                    }
                    else -> {

                    }
                }
            }

            val creditParams = saleSaveParams.copy(
                receiptStatus = creditAdvanceHolder.status,
                totalPaid = totalPaid,
                totalCash = totalCash,
                totalCard = totalCard,
                receiptPayments = receiptPayments,
                receiptUid = null,
                baseStatus = creditAdvanceHolder.status
            )

            return receiptSaleSaveRepository
                .createReceipt(creditParams)
                .catch {
                    if (it is PrinterException) {
                        saleInteractor.setReceiptCreated(true)
                        throw it
                    }
                    if (it is JsonDecodingException) throw SynchronizationException()
                    if (it is SamModuleBrokenException) throw SamModuleBrokenException()
                    else throw it
                }
                .flatMapResult()
                .flowOn(coroutineContextManager.ioContext)
        } else {
            return flowOf(Result.failure(NotImplementedError("CreditAdvanceHolder not defined")))
        }
    }

    @FlowPreview
    fun saveProductMarkings(): Flow<Result<Unit>> {
        val productMarkings: MutableList<ProductMarking> = ArrayList()
        saleInteractor.getReceiptDetails()
            .filter { !it.marks.isNullOrEmpty() }
            .forEach { detail ->
                detail.marks!!.forEach {
                    productMarkings.add(ProductMarking(productId = detail.productId, marking = it))
                }
            }
        return productMarkingInteractor.saveProductMarkings(productMarkings)
    }

    fun printLastReceipt(): Flow<Result<Unit>> {
        return receiptSaleSaveRepository
            .printLastReceipt()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun clearSaleData(): Flow<Unit> {
        return receiptSaleSaveRepository.clearTempData(false)
            .onEach { saleInteractor.clear() }
            .onEach { receiptHeldBroadcastChannel.send(Unit) }
            .flowOn(coroutineContextManager.ioContext)
    }

    fun clearTempDataIfNecessary(): Flow<Result<Unit>> {
        return if (creditAdvanceHolder != null) {
            if (!creditAdvanceHolder!!.isRepayment) {
                receiptSaleSaveRepository.clearTempData(
                    true
                )
                    .flatMapResult()
                    .flowOn(coroutineContextManager.ioContext)
            } else {
                flowOf(Result.failure(NotImplementedError("Repayment")))
            }
        } else {
            flowOf(Result.failure(NotImplementedError("CreditAdvanceProps not defined")))
        }
    }

}