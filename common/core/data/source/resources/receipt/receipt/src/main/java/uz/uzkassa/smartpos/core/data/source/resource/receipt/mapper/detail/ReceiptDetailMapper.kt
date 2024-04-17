package uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.detail

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetail
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetailEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetailResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatusResponse
import java.math.BigDecimal


fun List<ReceiptDetail>.mapToEntities(receiptUid: String) =
    map { it.mapToEntity(receiptUid) }

fun List<ReceiptDetail>.mapToResponses() =
    map { it.mapToResponse() }

fun ReceiptDetail.mapToEntity(receiptUid: String) =
    ReceiptDetailEntity(
        receiptUid = receiptUid,
        categoryId = categoryId,
        categoryName = categoryName,
        productId = productId,
        unitId = unit?.id,
        unitName = unit?.name,
        amount = amount,
        discountAmount = discountAmount,
        discountPercent = discountPercent,
        exciseAmount = exciseAmount,
        exciseRateAmount = exciseRateAmount,
        marks = marks,
        vatAmount = vatAmount,
        vatPercent = vatRate,
        quantity = quantity,
        price = price,
        status = status.name,
        barcode = barcode,
        name = name,
        vatBarcode = vatBarcode,
        committentTin = committentTin,
        label = label
    )

fun ReceiptDetail.mapToResponse() =
    ReceiptDetailResponse(
        categoryId = categoryId,
        categoryName = categoryName,
        productId = productId,
        unitId = unit?.id,
        unitName = unit?.name,
        amount = amount,
        discountAmount = discountAmount,
        discountPercent = discountPercent,
        exciseAmount = exciseAmount,
        exciseRateAmount = exciseRateAmount,
        marks = marks,
        vatAmount = vatAmount,
        vatPercent = vatRate?.toDouble(),
        quantity = quantity,
        price = price,
        status = ReceiptStatusResponse.valueOf(status.name),
        barcode = barcode,
        name = name,
        vatBarcode = vatBarcode,
        committentTin = committentTin,
        label = label
    )