package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sale.cart

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import uz.uzkassa.smartpos.core.data.source.resource.marking.dao.ProductMarkingDao
import uz.uzkassa.smartpos.core.data.utils.vat.VATAllocator
import uz.uzkassa.smartpos.core.utils.collections.replace
import uz.uzkassa.smartpos.core.utils.math.multiply
import uz.uzkassa.smartpos.core.utils.math.sum
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.sale.SaleCartBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.mapper.cart.mapToItemType
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.quantity.ProductQuantity
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleCart.ItemType
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleProductQuantity
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.SaleInteractor
import java.math.BigDecimal
import javax.inject.Inject

internal class SaleCartInteractor @Inject constructor(
    private val productMarkingDao: ProductMarkingDao,
    private val saleInteractor: SaleInteractor,
    private val saleCartBroadcastChannel: SaleCartBroadcastChannel
) {
    private val saleCartItemTypes: MutableList<ItemType> = arrayListOf()

    val actualAmount: BigDecimal
        get() {
            val amount: BigDecimal = saleInteractor.getTotalCost()
            return saleInteractor.getSaleDiscount()
                ?.let { amount - it.getOrCalculateDiscountAmount } ?: amount
        }

    private val paidAmount: BigDecimal
        get() = saleInteractor.getReceiptPayments().map { it.amount }.sum()

    private val leftAmount: BigDecimal
        get() = (actualAmount - paidAmount)
            .let { if (it < BigDecimal.ZERO) BigDecimal.ZERO else it }

    fun isSaleAllowed(): Boolean =
        saleInteractor.isSaleAllowed()

    fun getSelectedProductMarkedMarkings(quantity: ProductQuantity): Array<String> {
        val itemType: ItemType.Product? = saleCartItemTypes.filterIsInstance<ItemType.Product>()
            .find {
                val isSameProduct = it.productId?.equals(quantity.productId)
                val isSameUnit = it.unit?.id?.equals(quantity.unit?.id)
                val isSameUid = it.uid == quantity.uid
                return@find if (isSameProduct == null && isSameUnit == null) isSameUid
                else isSameProduct == true && isSameUnit == true
            }
        return itemType?.markings ?: arrayOf()
    }

    fun setItemTypes(itemTypes: List<ItemType>) {
        saleInteractor.setItemTypes(itemTypes)
        saleCartItemTypes.apply {
            clear();
            addAll(itemTypes)
        }
        saleCartBroadcastChannel.sendBlocking(saleCartItemTypes)
    }

    fun updateProduct(quantity: SaleProductQuantity): Boolean {
        return with(quantity) {
            val itemType: ItemType.Product? =
                saleCartItemTypes.filterIsInstance<ItemType.Product>()
                    .find {
                        if (uid != null) return@find it.uid == uid
                        val isSameProduct: Boolean = it.productId == productId
                        val isSameProductUnit: Boolean = it.unit?.id == unit?.id
                        return@find isSameProduct && isSameProductUnit
                    }

            return@with if (itemType != null) {
                val isSameMarking = itemType.hasMark && itemType.markings.none {
                    this.markings.contains(it).not()
                }

                if (isSameMarking.not()) {
                    val realAmount = if (this.quantity > 0) amount else -amount

                    val totalAmount = itemType.amount + realAmount

                    val totalQuantity = itemType.quantity + this.quantity

                    val totalMarkings: Array<String> = if (this.quantity < 0) {
                        itemType.markings.filter { this.markings.contains(it).not() }.toTypedArray()
                    } else {
                        itemType.markings + this.markings
                    }
                    val isMarkingAvailable = markings.isNullOrEmpty()

                    val amountForVat = if (productPrice == price) productPrice else price

                    val quantityForVat = if (isMarkingAvailable) totalQuantity else 1.0

                    val vatAllocator = VATAllocator(amountForVat, quantityForVat, itemType.vatRate)
                    val vatAmount =
                        if (isMarkingAvailable)
                            vatAllocator.vatAmount
                        else
                            vatAllocator.vatAmount.multiply(totalQuantity)

                    updateItemType(
                        itemType = itemType,
                        updatedItemType = itemType.copy(
                            amount = totalAmount,
                            quantity = totalQuantity,
                            price = price,
                            unit = unit,
                            vatAmount = vatAmount,
                            markings = totalMarkings
                        )
                    )
                } else true
            } else
                addItemType(quantity.mapToItemType())
        }
    }

    fun upsertProduct(quantity: SaleProductQuantity): Boolean {
        saleCartItemTypes.filterIsInstance<ItemType.Product>()
            .find {
                val isSameProduct = it.productId?.equals(quantity.product?.id)
                val isSameUnit = it.unit?.id?.equals(quantity.lastUnitId)
                return@find if (isSameProduct == null && isSameUnit == null) it.uid == quantity.uid
                else isSameProduct == true && isSameUnit == true
            }?.let {
                if (!isAmountAllowed(it.amount, quantity.amount)) return false
                else {
                    saleCartItemTypes.remove(it)
                    saleInteractor.deleteItemType(it)
                }
            }

        return updateProduct(quantity)
    }

    fun deleteItemType(itemType: ItemType): Boolean {
        if (itemType.amount > leftAmount) return false

        if (itemType is ItemType.Product) {
            runBlocking(Dispatchers.IO) {
                itemType.productId?.let { productMarkingDao.deleteProductMarkingByProductId(it) }
            }
        }

        saleInteractor.deleteItemType(itemType)
        saleCartItemTypes.remove(itemType)
        if (saleCartItemTypes.isEmpty()) saleInteractor.clear()
        saleCartBroadcastChannel.sendBlocking(saleCartItemTypes)
        return true
    }

    fun clear() {
        saleCartItemTypes.clear()
    }

    private fun addItemType(itemType: ItemType): Boolean {
        saleInteractor.setItemType(itemType)
        saleCartItemTypes.add(0, itemType)
        saleCartBroadcastChannel.sendBlocking(saleCartItemTypes)
        return true
    }

    private fun updateItemType(itemType: ItemType, updatedItemType: ItemType): Boolean {
        if (!isAmountAllowed(itemType.amount, updatedItemType.amount)) return false

        saleInteractor.setItemType(updatedItemType)
        saleCartItemTypes.replace(itemType, updatedItemType)
        saleCartBroadcastChannel.sendBlocking(saleCartItemTypes)
        return true
    }

    private fun isAmountAllowed(currentAmount: BigDecimal, newAmount: BigDecimal): Boolean {
        if (actualAmount == BigDecimal.ZERO || leftAmount == BigDecimal.ZERO) return true
        val actualOtherAmount: BigDecimal = actualAmount - currentAmount
        val actualLeftAmount: BigDecimal = actualAmount - currentAmount + newAmount - paidAmount

        if (actualLeftAmount < BigDecimal.ZERO) return false

        return newAmount >= currentAmount
                || (newAmount >= paidAmount
                || actualLeftAmount >= BigDecimal.ZERO &&
                (actualOtherAmount >= actualLeftAmount || newAmount >= actualLeftAmount))
    }

    fun getTotalMarkings(): Array<String> {
        val totalMarkedMarkings: MutableList<String> = ArrayList()
        saleInteractor.getReceiptDetails()
            .filter { !it.marks.isNullOrEmpty() }
            .forEach { detail ->
                detail.marks!!.forEach {
                    totalMarkedMarkings.add(it)
                }
            }
        return totalMarkedMarkings.toTypedArray()
    }
}