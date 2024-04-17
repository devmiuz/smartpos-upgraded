package uz.uzkassa.smartpos.feature.user.cashier.refund.data.mapper.receipt

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetail
import uz.uzkassa.smartpos.core.utils.primitives.roundToString
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.cart.RefundCart
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.model.quantity.ProductQuantity

internal fun RefundCart.Product.mapToReceiptDetail(): ReceiptDetail {
    return ReceiptDetail(
        categoryId = categoryId,
        categoryName = categoryName,
        productId = productId,
        amount = amount,
        discountAmount = discountAmount,
        discountPercent = discountPercent,
        exciseAmount = exciseAmount,
        exciseRateAmount = exciseRateAmount,
        vatAmount = vatAmount,
        vatRate = vatRate?.roundToString()?.toDouble(),
        quantity = quantity,
        price = price,
        status = status,
        unit = unit,
        marks = markedMarkings,
        barcode = barcode,
        name = name,
        vatBarcode = vatBarcode,
        committentTin = committentTin,
        vatPercent = vatPercent,
        unitId = unitId,
        label = label
    )
}

internal fun RefundCart.Product.mapToProductQuantity(): ProductQuantity {
    return ProductQuantity(
        uid = uid,
        categoryId = categoryId,
        categoryName = categoryName,
        productId = productId,
        amount = amount,
        price = price,
        productPrice = productPrice,
        vatRate = vatRate,
        quantity = quantity,
        maxQuantity = detailQuantity,
        lastUnitId = detailUnit?.id,
        unit = unit,
        barcode = barcode,
        productName = name,
        markedMarkings = markedMarkings,
        totalMarkings = totalMarkings
    )
}