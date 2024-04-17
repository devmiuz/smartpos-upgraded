package uz.uzkassa.smartpos.feature.user.cashier.refund.domain.process

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.manager.device.model.DeviceLocationInfo
import uz.uzkassa.smartpos.core.data.manager.printer.exception.PrinterException
import uz.uzkassa.smartpos.core.data.source.fiscal.model.receipt.ReceiptRefundInfo
import uz.uzkassa.smartpos.core.data.source.resource.marking.model.ProductMarking
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetail
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.core.utils.math.sum
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.amount.AmountType
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.payment.amount.RefundReceiptPayment
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.process.RefundProcessDetails
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.receipt.save.ReceiptSaveRepository
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.receipt.save.params.ReceiptSaveParams
import uz.uzkassa.smartpos.feature.user.cashier.refund.domain.RefundInteractor
import uz.uzkassa.smartpos.feature.user.cashier.refund.domain.product.marking.ProductMarkingInteractor
import java.math.BigDecimal
import javax.inject.Inject

internal class RefundProcessInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val deviceInfoManager: DeviceInfoManager,
    private val receiptSaveRepository: ReceiptSaveRepository,
    private val refundInteractor: RefundInteractor,
    private val productMarkingInteractor: ProductMarkingInteractor
) {
    private var params: ReceiptSaveParams? = null

    private val details: List<ReceiptDetail> by lazy {
        refundInteractor.getReceiptDetails()
    }
    private val refundTotalAmount by lazy {
        details.map {
            it.amount
        }.sum()
    }

    fun getRefundReceiptDetails(): RefundProcessDetails =
        RefundProcessDetails(refundTotalAmount)

    @FlowPreview
    fun createReceipt(): Flow<Result<Unit>> {
        return getActualParams()
            .flatMapConcat { params ->
                receiptSaveRepository
                    .createReceipt(params)
                    .catch {
                        if (it is PrinterException)
                            refundInteractor.setReceiptCreated(true)
                        throw it
                    }
            }.flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun printLastReceipt(): Flow<Result<Unit>> {
        return receiptSaveRepository
            .printLastReceipt()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun deleteProductMarkings(): Flow<Result<Unit>> {
        val productMarkings: MutableList<ProductMarking> = ArrayList()
        refundInteractor.getReceiptDetails()
            .filter { !it.marks.isNullOrEmpty() }
            .forEach { detail ->
                detail.marks!!.forEach {
                    productMarkings.add(ProductMarking(productId = detail.productId, marking = it))
                }
            }
        return productMarkingInteractor.deleteProductMarkings(productMarkings)
    }

    fun clearSaleData(): Flow<Unit> {
        return receiptSaveRepository.clearTempData()
            .flowOn(coroutineContextManager.ioContext)
    }

    private fun getActualParams(): Flow<ReceiptSaveParams> {
        return flow {
            if (params == null) {
                val deviceLocationInfo: DeviceLocationInfo? = deviceInfoManager.locationInfo
                val details: List<ReceiptDetail> =
                    details.filter { it.status == ReceiptStatus.RETURNED && it.quantity > 0.0 }
                val payments: List<RefundReceiptPayment> = refundInteractor.getReceiptPayments()

                val totalCard: BigDecimal =
                    payments.filter { it.type == AmountType.CARD }.map { it.amount }.sum()
                val totalCash: BigDecimal =
                    payments.filter { it.type == AmountType.CASH }.map { it.amount }.sum().let {
                        return@let if (totalCard > BigDecimal.ZERO) refundTotalAmount - totalCard
                        else refundTotalAmount
                    }

                val totalPaid: BigDecimal = payments.map { it.amount }.sum()

                params = ReceiptSaveParams(
                    uniqueId = refundInteractor.getUniqueId(deviceInfoManager.deviceInfo.serialNumber),
                    originUid = refundInteractor.getReceiptUid(),
                    latitude = deviceLocationInfo?.latitude,
                    longitude = deviceLocationInfo?.longitude,
                    totalCard = totalCard,
                    totalCash = totalCash,
                    totalCost = refundTotalAmount,
                    totalPaid = totalPaid,
                    receiptStatus = ReceiptStatus.RETURNED,
                    receiptDetails = details,
                    receiptRefundInfo = refundInteractor.getRefundInfo(),
                    extraInfo = null
                )
                emit(checkNotNull(params))
            }
        }
    }
}