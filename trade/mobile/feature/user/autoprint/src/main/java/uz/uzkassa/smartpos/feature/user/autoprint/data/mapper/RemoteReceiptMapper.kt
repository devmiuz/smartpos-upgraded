package uz.uzkassa.smartpos.feature.user.autoprint.data.mapper

import uz.uzkassa.smartpos.feature.user.autoprint.data.models.RemoteReceiptDetailResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetail
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import java.math.BigDecimal

fun RemoteReceiptDetailResponse.mapToReceiptDetail() =
    ReceiptDetail(
        categoryId = null,
        categoryName = null,
        productId = productId,
        amount = amount ?: BigDecimal.ZERO,
        discountAmount = BigDecimal.ZERO,
        discountPercent = 0.0,
        exciseAmount = BigDecimal.ZERO,
        exciseRateAmount = BigDecimal.ZERO,
        marks = null,
        vatAmount = vatAmount,
        vatRate = vatPercent,
        quantity = quantity ?: 0.0,
        price = price ?: BigDecimal.ZERO,
        status = ReceiptStatus.DRAFT,
        unit = null,
        barcode = productBarcode,
        vatBarcode = null,
        name = productName.toString(),
        committentTin = null,
        vatPercent = vatPercent,
        unitId = unitId,
        label = null
    )