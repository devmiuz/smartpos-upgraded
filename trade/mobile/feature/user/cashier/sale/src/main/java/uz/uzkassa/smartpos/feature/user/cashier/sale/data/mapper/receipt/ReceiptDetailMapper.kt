package uz.uzkassa.smartpos.feature.user.cashier.sale.data.mapper.receipt

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetail
import uz.uzkassa.smartpos.core.utils.primitives.roundToString
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.sale.cart.SaleCart

@Suppress("UNREACHABLE_CODE")
internal fun SaleCart.ItemType.Product.mapToReceiptDetail(): ReceiptDetail {
    return ReceiptDetail(
        categoryId = categoryId,
        categoryName = categoryName,
        productId = productId,
        amount = amount,
        discountAmount = discountAmount,
        discountPercent = discountPercent,
        exciseAmount = exciseAmount,
        exciseRateAmount = exciseRateAmount,
        marks = markings,
        vatAmount = vatAmount,
        vatRate = vatRate?.roundToString()?.toDouble(),
        quantity = quantity,
        price = price,
        status = status,
        unit = unit,
        barcode = barcode,
        name = name,
        vatBarcode = vatBarcode,
        committentTin = committentTin,
        vatPercent = vatPercent,
        unitId = unitId,
        label = label
    )
}