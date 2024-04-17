package uz.uzkassa.smartpos.feature.user.cashier.refund.data.mapper.cart

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetail
import uz.uzkassa.smartpos.core.data.utils.vat.VATAllocator
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.cart.RefundCart
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.quantity.ProductQuantity
import java.math.BigDecimal
import java.math.RoundingMode

internal fun ProductQuantity.mapToProduct(product: RefundCart.Product): RefundCart.Product {
    val discountForQuantity = price
        .multiply(BigDecimal(product.discountPercent))
        .divide(BigDecimal(100), 7, RoundingMode.HALF_UP)

    val residualPrice = price.subtract(discountForQuantity)

    val discountForChangedQuantity = discountForQuantity.multiply(BigDecimal(quantity))

    val amountForChangedQuantity = residualPrice.multiply(BigDecimal(quantity))

    val vatAllocator = VATAllocator(residualPrice, quantity, product.vatRate)

    return product.copy(
        amount = amountForChangedQuantity,
        vatAmount = vatAllocator.vatAmount,
        vatRate = vatRate,
        quantity = quantity,
        unit = unit,
        discountAmount = discountForChangedQuantity
    )
}

internal fun ReceiptDetail.mapToProduct(): RefundCart.Product {
    var vatRate: BigDecimal? = null
    if (this.vatRate != null) vatRate = BigDecimal(this.vatRate!!)
    val actualAmount =
        if (amount > BigDecimal.ZERO && amount >= discountAmount) amount - discountAmount
        else amount

    return RefundCart.Product(
        categoryId = categoryId,
        categoryName = categoryName,
        productId = productId,
        detailAmount = amount,
        amount = BigDecimal.ZERO,
        discountAmount = discountAmount,
        discountPercent = discountPercent,
        exciseAmount = exciseAmount,
        exciseRateAmount = exciseRateAmount,
        vatAmount = vatAmount,
        vatRate = vatRate,
        detailQuantity = quantity,
        quantity = 0.0,
        price = price,
        productPrice = price,
        status = status,
        detailUnit = unit,
        unit = unit,
        forRefund = false,
        barcode = barcode,
        name = name,
        vatBarcode = vatBarcode,
        markedMarkings = if (marks.isNullOrEmpty()) null else arrayOf(),
        totalMarkings = marks,
        committentTin = committentTin,
        vatPercent = vatPercent,
        unitId = unitId,
        label = label
    )
}