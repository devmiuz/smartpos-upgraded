package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.receipt.draft.creation

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import uz.uzkassa.smartpos.core.data.source.resource.marking.model.ProductMarking
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.receipt.held.ReceiptHeldBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.exception.receipt.ReceiptDraftNameNotInputtedException
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.receipt.ReceiptDraftDetails
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft.ReceiptDraftRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.draft.params.ReceiptDraftSaveParams
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.SaleInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.product.marking.ProductMarkingInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.receipt.ReceiptSaveInteractor
import java.math.BigDecimal
import javax.inject.Inject

internal class ReceiptDraftCreationInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val receiptDraftRepository: ReceiptDraftRepository,
    private val receiptHeldBroadcastChannel: ReceiptHeldBroadcastChannel,
    private val receiptSaveInteractor: ReceiptSaveInteractor,
    private val saleInteractor: SaleInteractor,
    private val productMarkingInteractor: ProductMarkingInteractor
) {
    private var name: String? = null

    fun getReceiptDraftDetails(): ReceiptDraftDetails {
        with(saleInteractor) {
            val totalCost: BigDecimal = getTotalCost()
            val amount: BigDecimal =
                getSaleDiscount()?.let { totalCost - it.getOrCalculateDiscountAmount } ?: totalCost
            return ReceiptDraftDetails(getReceiptDetails().size, amount)
        }
    }

    fun setDraftName(receiptName: String) {
        this.name = receiptName
    }

    fun setCustomerName(name: String) {
        this.saleInteractor.setCustomerName(name)
    }

    fun setCustomerContact(contact: String) {
        this.saleInteractor.setCustomerContact(contact)
    }

    fun clearSaleData() = saleInteractor.clear()

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun createReceiptDraft(): Flow<Result<Unit>> = when {
        name.isNullOrBlank() -> flowOf(Result.failure(ReceiptDraftNameNotInputtedException()))
        else -> {
            val params = ReceiptDraftSaveParams(
                receiptSaveParams = receiptSaveInteractor.getParams(ReceiptStatus.DRAFT),
                name = checkNotNull(name)
            )

            receiptDraftRepository
                .createReceiptDraft(params)
                .onEach {
                    saleInteractor.clear()
                    receiptHeldBroadcastChannel.send(Unit)
                }
                .flatMapResult()
                .flowOn(coroutineContextManager.ioContext)
        }
    }

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
}