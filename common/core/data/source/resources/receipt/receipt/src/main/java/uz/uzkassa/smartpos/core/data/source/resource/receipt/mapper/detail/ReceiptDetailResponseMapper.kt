package uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.detail

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetail
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetailEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetailResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatusResponse
import uz.uzkassa.smartpos.core.data.source.resource.unit.model.Unit
import java.math.BigDecimal

fun List<ReceiptDetailResponse>.mapToEntities(receiptUid: String) =
    map { it.mapToEntity(receiptUid) }

fun ReceiptDetailResponse.mapToEntity(receiptUid: String) =
    ReceiptDetailEntity(
        receiptUid = receiptUid,
        categoryId = categoryId,
        categoryName = categoryName,
        productId = productId,
        unitId = unitId,
        unitName = unitName,
        amount = amount,
        discountAmount = discountAmount ?: BigDecimal.ZERO,
        discountPercent = discountPercent ?: 0.0,
        exciseAmount = exciseAmount,
        marks = marks ?: emptyArray(),
        exciseRateAmount = exciseRateAmount,
        vatAmount = vatAmount,
        vatPercent = vatPercent,
        vatBarcode = vatBarcode,
        quantity = quantity,
        price = price,
        status = (status ?: ReceiptStatusResponse.PAID).name,
        barcode = barcode,
        name = name,
        committentTin = committentTin,
        label = label
    )


fun ReceiptDetailResponse.mapToReceiptDetail() =
    ReceiptDetail(
        categoryId = categoryId,
        categoryName = categoryName,
        productId = productId,
        unitId = unitId,
        unit = null,
        amount = amount,
        discountAmount = discountAmount ?: BigDecimal.ZERO,
        discountPercent = discountPercent ?: 0.0,
        exciseAmount = exciseAmount,
        marks = marks ?: emptyArray(),
        exciseRateAmount = exciseRateAmount,
        vatAmount = vatAmount,
        vatPercent = vatPercent,
        vatBarcode = vatBarcode,
        quantity = quantity,
        price = price,
        status = ReceiptStatus.DRAFT,
        barcode = barcode,
        name = "test",
        committentTin = committentTin,
        label = label,
        vatRate = 0.0
    )