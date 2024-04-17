package uz.uzkassa.smartpos.feature.user.cashier.sale.domain

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetail
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.data.utils.vat.VATAllocator
import uz.uzkassa.smartpos.core.utils.collections.removeBy
import uz.uzkassa.smartpos.core.utils.collections.replace
import uz.uzkassa.smartpos.core.utils.math.multiply
import uz.uzkassa.smartpos.core.utils.math.sum
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.mapper.receipt.mapToReceiptDetail
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.credit.CreditAdvanceHolder
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.discount.DiscountType
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleCart.ItemType
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.payment.discount.SaleDiscount
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.transaction.TransactionHolder
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

internal class SaleInteractor {

    private val receiptDetails: MutableList<ItemType> = arrayListOf()
    private val receiptPayments: MutableList<ReceiptPayment> = arrayListOf()

    private var uniqueId: String? = Date().time.toString()
    private var serialNumber: String? = null

    private var billId: String? = null

    var creditAdvanceHolder: CreditAdvanceHolder? = null

    var transactionHolder: TransactionHolder? = null

//    fun setCreditAdvanceHolder(creditAdvanceHolder: CreditAdvanceHolder?) {
//        this.creditAdvanceHolder = creditAdvanceHolder
//    }
//
//    fun getCreditAdvanceHolder() = creditAdvanceHolder

    fun getPaymentBillId() = billId

    fun getUniqueId(serialNumber: String): String {
        this.serialNumber =
            if (serialNumber.length >= 6) serialNumber.substring(serialNumber.length - 6) else serialNumber
        return this.serialNumber + uniqueId
    }

    /**
     * isSaleAllowed variable used for disallow sale
     * Occurred: when shift finishing failed during print shift details
     *
     * Required: finish current shift
     * */
    private var isSaleAllowed: Boolean = true

    /**
     * isReceiptCreated variable used for disallow edit sale
     * Occurred: when print error occurred during sale printing
     *
     * Required: finish current sale
     * */
    private var isReceiptCreated: Boolean = false
    private var draftId: Long? = null
    private var uid: String? = null
    private var saleDiscount: SaleDiscount? = null


    private var customerName: String? = null
    private var customerContact: String? = null
    private var readonly: Boolean? = false
    private var forceToPrint: Boolean? = false

    /**
     * isPaymentReceived variable used for disallow finish/pause shift
     * Occurred: when exist received payment or discount
     *
     * Required: finish current sale
     * */
    val isPaymentReceived: Boolean
        get() = receiptPayments.find { it.amount > BigDecimal.ZERO }?.let { true }
            ?: saleDiscount?.getOrCalculateDiscountAmount?.let { it > BigDecimal.ZERO } ?: false

    val receiptDraftId: Long?
        get() = draftId

    val receiptUid: String?
        get() = uid

    val leftAmount: BigDecimal
        get() = (getTotalCost() - receiptPayments.map { it.amount }.sum())
            .let { if (it < BigDecimal.ZERO) BigDecimal.ZERO else it }

    fun setReadonly(readonly: Boolean) {
        this.readonly = readonly
    }

    fun getReadonly() = this.readonly

    fun getForceToPrint() = this.forceToPrint

    fun setForceToPrint(forceToPrint: Boolean) {
        this.forceToPrint = forceToPrint
    }

    fun setCustomerName(customerName: String) {
        this.customerName = customerName
    }

    fun getCustomerName() = this.customerName

    fun getCustomerContact() = this.customerContact

    fun setCustomerContact(customerContact: String) {
        this.customerContact = customerContact
    }

    fun getReceiptDetails(): List<ReceiptDetail> =
        receiptDetails.filterIsInstance<ItemType.Product>()
            .calculateDiscount()
            .map {
                it.mapToReceiptDetail()
            }

    fun getReceiptPayments(): List<ReceiptPayment> =
        receiptPayments

    fun getSaleDiscount(): SaleDiscount? =
        saleDiscount

    fun getTotalCost(): BigDecimal {
        var totalCost = receiptDetails.map { it.amount }.sum()
        if (creditAdvanceHolder != null) {
            if (creditAdvanceHolder!!.isRepayment) {
                totalCost = creditAdvanceHolder!!.paymentAmount
            }
            if (creditAdvanceHolder!!.status == ReceiptStatus.ADVANCE) {
                totalCost = creditAdvanceHolder!!.paymentAmount
            }
        }
        return totalCost
    }

    fun isReceiptCreated(): Boolean =
        isReceiptCreated

    fun setReceiptCreated(value: Boolean) {
        isReceiptCreated = value
    }

    fun isSaleAllowed(): Boolean =
        isSaleAllowed

    fun setAllowSale(value: Boolean) {
        isSaleAllowed = value
    }

    fun clear() {
        uniqueId = Date().time.toString()
        billId = null
        receiptDetails.clear()
        receiptPayments.clear()
        isReceiptCreated = false
        draftId = null
        uid = null
        saleDiscount = null
        creditAdvanceHolder = null
        transactionHolder = null
    }

    fun deleteItemType(itemType: ItemType) {
        if (itemType !is ItemType.Product) return
        receiptDetails.removeBy { itemType.uid == it.uid }
        saleDiscount = saleDiscount?.recalculate()
    }

    fun setItemType(itemType: ItemType) {
        deleteItemType(itemType)
        if (itemType !is ItemType.Product) return
        receiptDetails.add(itemType)
        saleDiscount = saleDiscount?.recalculate()
    }

    fun setItemTypes(itemTypes: List<ItemType>) {
        receiptDetails.apply {
            clear();
            addAll(itemTypes)
        }
        saleDiscount = saleDiscount?.recalculate()
    }

    fun setReceiptDraftId(receiptDraftId: Long) {
        draftId = receiptDraftId
    }

    fun setReceiptUid(uid: String?) {
        this.uid = uid
    }

    fun setReceiptPayment(payment: ReceiptPayment) {
        receiptPayments.find { it.type == payment.type }
            ?.let { receiptPayments.replace(it, payment.copy(amount = payment.amount + it.amount)) }
            ?: receiptPayments.add(payment)
    }

    fun setReceiptPayments(payments: List<ReceiptPayment>) {
        receiptPayments.apply {
            clear()
            addAll(payments)
        }
    }

    fun setSaleDiscount(
        discountAmount: BigDecimal,
        discountPercent: Double,
        discountType: DiscountType
    ) {
        val totalPaid: BigDecimal = receiptPayments.map { it.amount }.sum()
        val totalAmount = getTotalCost() - totalPaid
        this.saleDiscount = SaleDiscount(totalAmount, discountAmount, discountPercent, discountType)
    }

    private fun SaleDiscount.recalculate(): SaleDiscount {
        return if (discountType == DiscountType.BY_SUM) {
            copy(
                totalAmount = leftAmount,
                discountAmount = discountAmount,
                discountPercent = discountPercent,
                discountType = discountType
            )
        } else {
            copy(
                totalAmount = leftAmount,
                discountAmount = leftAmount.multiply(discountPercent)
                    .divide(BigDecimal(10), 7, RoundingMode.HALF_UP),
                discountPercent = discountPercent,
                discountType = discountType
            )
        }
    }

    private fun List<ItemType.Product>.calculateDiscount(): List<ItemType.Product> {
        if (saleDiscount == null) return this
        val saleDiscount: SaleDiscount = checkNotNull(saleDiscount)
        val discountAmount: BigDecimal = saleDiscount.getOrCalculateDiscountAmount

        return map {
            if (discountAmount <= BigDecimal.ZERO) return@map it

            var discountPercentPerProduct: Double = saleDiscount.discountPercent
            if (discountPercentPerProduct == 0.0) {
                discountPercentPerProduct =
                    discountAmount
                        .multiply(BigDecimal(100))
                        .divide(getTotalCost(), 9, RoundingMode.HALF_UP)
                        .toDouble()
            }

            val discountAmountPerQuantity: BigDecimal =
                it.price
                    .multiply(discountPercentPerProduct)
                    .divide(BigDecimal(100), 7, RoundingMode.HALF_UP)

            val residualPrice = it.price - discountAmountPerQuantity

            val vatAllocator = VATAllocator(
                amount = residualPrice,
                quantity = if (!it.hasMark) it.quantity else 1.0,
                vatRate = it.vatRate
            )

            val vatAmount =
                if (!it.hasMark) vatAllocator.vatAmount else vatAllocator.vatAmount.multiply(it.quantity)


            return@map it.copy(
                discountAmount = discountAmountPerQuantity.multiply(it.quantity),
                discountPercent = discountPercentPerProduct,
                vatAmount = vatAmount
            )
        }
    }

    fun setPaymentBillId(billId: String) {
        this.billId = billId
    }
}