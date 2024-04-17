package uz.uzkassa.smartpos.feature.user.cashier.sale.data.mapper.cart

import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetail
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import uz.uzkassa.smartpos.core.data.utils.vat.VATAllocator
import uz.uzkassa.smartpos.core.utils.math.multiply
import uz.uzkassa.smartpos.core.utils.primitives.roundToBigDecimal
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleCart
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleProductQuantity
import java.math.BigDecimal

internal fun Product.mapToItemType(
    amount: BigDecimal,
    quantity: Double,
    price: BigDecimal,
    unit: Unit?
): SaleCart.ItemType {
    val vatAllocator = VATAllocator(price, quantity, vatRate)
    var vatRate: BigDecimal? = null
    if (vatAllocator.vatRate != null) {
        vatRate = BigDecimal(vatAllocator.vatRate!!)
    }
    return SaleCart.ItemType.Product(
        categoryId = category?.id,
        categoryName = category?.name,
        productId = id,
        barcode = barcode,
        amount = amount,
        discountAmount = BigDecimal.ZERO,
        discountPercent = 0.0,
        exciseAmount = exciseAmount?.multiply(BigDecimal(quantity)),
        exciseRateAmount = exciseAmount,
        hasMark = hasMark,
        vatAmount = vatAllocator.vatAmount,
        vatRate = vatRate,
        availableQuantity = count.let { if (it > 0.0) it - quantity else it },
        quantity = quantity,
        price = price,
        productPrice = price,
        status = ReceiptStatus.PAID,
        unit = unit,
        name = name,
        markings = arrayOf(),
        vatBarcode = vatBarcode,
        committentTin = commintentTin,
        vatPercent = vatPercent,
        unitId = unitId,
        label = label
    )
}

internal fun SaleProductQuantity.mapToItemType(): SaleCart.ItemType {
    val amountForVat = if (productPrice == price) productPrice else price
    val quantityForVat = if (markings.isNullOrEmpty()) quantity else 1.0
    val vatRate = product?.vatRate ?: vatRate
    val vatAllocator = VATAllocator(amountForVat, quantityForVat, vatRate)
    val vatAmount =
        if (markings.isNullOrEmpty()) vatAllocator.vatAmount else vatAllocator.vatAmount.multiply(
            quantity
        )

    return SaleCart.ItemType.Product(
        uid = uid,
        categoryId = categoryId,
        categoryName = categoryName,
        productId = productId,
        amount = amount,
        discountAmount = BigDecimal.ZERO,
        discountPercent = 0.0,
        exciseAmount = product?.exciseAmount?.multiply(BigDecimal(quantity)),
        exciseRateAmount = product?.exciseAmount,
        hasMark = product?.hasMark ?: false,
        vatAmount = vatAmount,
        vatRate = vatRate,
        availableQuantity = quantity,
        quantity = quantity,
        price = price,
        productPrice = productPrice,
        status = ReceiptStatus.PAID,
        unit = unit ?: product?.unit,
        barcode = barcode ?: product?.barcode,
        name = product?.name ?: productName,
        markings = markings,
        vatBarcode = vatBarcode,
        committentTin = committentTin,
        vatPercent = vatPercent,
        unitId = unitId,
        label = label
    )
}

internal fun ReceiptDetail.mapToItemType(product: Product?): SaleCart.ItemType {
    val productExciseAmount: BigDecimal? = product?.exciseAmount
    val vatAllocator = VATAllocator(
        amount = price,
        quantity = quantity,
        vatRate = product?.vatRate ?: vatRate?.roundToBigDecimal()
    )
    var vatRate: BigDecimal? = null
    if (vatAllocator.vatRate != null) {
        vatRate = BigDecimal(vatAllocator.vatRate!!)
    }
    return SaleCart.ItemType.Product(
        categoryId = product?.category?.id,
        categoryName = product?.category?.name,
        productId = productId,
        barcode = product?.barcode ?: barcode,
        amount = amount,
        discountAmount = BigDecimal.ZERO,
        discountPercent = 0.0,
        exciseAmount = productExciseAmount?.multiply(BigDecimal(quantity))
            ?: exciseAmount?.multiply(BigDecimal(quantity)),
        exciseRateAmount = productExciseAmount ?: exciseAmount,
        hasMark = product?.hasMark ?: false,
        vatAmount = vatAllocator.vatAmount,
        vatRate = vatRate,
        availableQuantity = product?.count ?: 0.0,
        quantity = quantity,
        price = price,
        productPrice = product?.salesPrice ?: BigDecimal.ZERO,
        status = ReceiptStatus.PAID,
        unit = unit,
        name = product?.name ?: name,
        markings = marks ?: emptyArray(),
        vatBarcode = vatBarcode,
        committentTin = committentTin,
        vatPercent = vatPercent,
        unitId = unitId,
        label = label
    )
}
