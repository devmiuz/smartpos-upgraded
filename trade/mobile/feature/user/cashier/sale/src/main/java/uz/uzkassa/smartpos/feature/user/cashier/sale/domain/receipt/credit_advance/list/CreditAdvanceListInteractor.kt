package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.receipt.credit_advance.list

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.Receipt
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetail
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.mapper.cart.mapToItemType
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.discount.DiscountType
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleCart
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.product.ProductRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.credit_advance.CreditAdvanceRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.SaleInteractor
import java.math.BigDecimal
import javax.inject.Inject

internal class CreditAdvanceListInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val creditAdvanceRepository: CreditAdvanceRepository,
    private val productRepository: ProductRepository,
    private val saleInteractor: SaleInteractor
) {

    var selectedCreditAdvanceReceipt: Receipt? = null

    fun getAdvanceCreditReceipts(): Flow<Result<List<Receipt>>> {
        return creditAdvanceRepository
            .getAdvanceReceipts()
            .zip(creditAdvanceRepository.getCreditReceipts()) { advanceReceips, creditReceipts ->
                advanceReceips + creditReceipts
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun getAdvanceReceiptByUid(uid: String): Flow<Result<Receipt>> {
        return creditAdvanceRepository
            .getAdvanceReceiptByUid(uid)
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    fun searchCreditAdvanceReceipt(
        customerName: String = "",
        customerPhone: String = "",
        fiscalUrl: String = "",
        receiptUid: String = ""
    ): Flow<Result<List<Receipt>>> {
        return creditAdvanceRepository
            .searchCreditAdvanceReceipts(
                receiptUid = receiptUid,
                fiscalUrl = fiscalUrl,
                customerPhone = customerPhone,
                customerName = customerName
            )
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun restoreCreditAdvanceReceipt(): Flow<Result<Unit>> {
        return flowOf(selectedCreditAdvanceReceipt)
            .onEach {
                with(saleInteractor) {
                    if (it!!.totalDiscount > BigDecimal.ZERO) {
                        setSaleDiscount(
                            it.totalDiscount,
                            it.discountPercent,
                            DiscountType.BY_PERCENT
                        )
                    }
                    if (it.baseStatus == ReceiptStatus.ADVANCE) {
                        setReceiptUid(it.originUid)
                    } else {
                        setReceiptUid(it.originUid)
                    }
                }
            }
            .flatMapConcat { it ->
                generateProduct(it!!.receiptDetails)
                    .onEach {
                        saleInteractor.setItemTypes(it)
                    }
            }
            .map { }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    @FlowPreview
    private suspend fun generateProduct(
        details: List<ReceiptDetail>
    ): Flow<List<SaleCart.ItemType.Product>> {
        return flowOf(details)
            .flatMapConcat { it ->
                val products: List<SaleCart.ItemType.Product?> =
                    it
                        .map { detail ->
                            if (detail.productId == null)
                                return@map detail.mapToItemType(null) as SaleCart.ItemType.Product
                            val productId: Long = requireNotNull(detail.productId)
                            val product = runCatching {
                                productRepository.getProductByProductId(productId).first()
                            }.getOrNull()
                            return@map detail.mapToItemType(product) as SaleCart.ItemType.Product
                        }
                return@flatMapConcat flowOf(products.filterNotNull())
            }
    }

    fun setSelectedReceipt(receipt: Receipt) {
        this.selectedCreditAdvanceReceipt = receipt
    }

    fun clearSaleInteractor() {
        saleInteractor.clear()
    }

    fun getCreditAdvanceHolderFromSale() = saleInteractor.creditAdvanceHolder

    fun setCreditAdvanceProps(creditAdvanceHolder: CreditAdvanceHolder) {
        saleInteractor.creditAdvanceHolder = creditAdvanceHolder
    }
}