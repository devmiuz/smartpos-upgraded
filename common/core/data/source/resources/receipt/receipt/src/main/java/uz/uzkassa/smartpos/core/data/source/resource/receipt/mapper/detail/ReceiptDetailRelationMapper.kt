package uz.uzkassa.smartpos.core.data.source.resource.receipt.mapper.detail

import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetail
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetailRelation
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.detail.ReceiptDetailResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatusResponse
import uz.uzkassa.smartpos.core.data.source.resource.unit.mapper.map

fun List<ReceiptDetailRelation>.map() =
    map { it.map() }

fun List<ReceiptDetailRelation>.mapToResponse() =
    map { it.mapToResponse() }

fun ReceiptDetailRelation.map() =
    ReceiptDetail(
        categoryId = categoryEntity?.id,
        categoryName = categoryEntity?.name,
        productId = receiptDetailEntity.productId,
        amount = receiptDetailEntity.amount,
        discountAmount = receiptDetailEntity.discountAmount,
        discountPercent = receiptDetailEntity.discountPercent,
        exciseAmount = receiptDetailEntity.exciseAmount,
        exciseRateAmount = receiptDetailEntity.exciseRateAmount,
        vatAmount = receiptDetailEntity.vatAmount,
        vatRate = receiptDetailEntity.vatPercent,
        marks = receiptDetailEntity.marks,
        quantity = receiptDetailEntity.quantity,
        price = receiptDetailEntity.price,
        status = ReceiptStatus.valueOf(receiptDetailEntity.status),
        unit = unitEntity?.map(),
        barcode = receiptDetailEntity.barcode,
        name = receiptDetailEntity.name,
        vatBarcode = receiptDetailEntity.vatBarcode,
        committentTin = receiptDetailEntity.committentTin,
        vatPercent = receiptDetailEntity.vatPercent,
        unitId = receiptDetailEntity.unitId,
        label = receiptDetailEntity.label
    )

fun ReceiptDetailRelation.mapToResponse() =
    ReceiptDetailResponse(
        categoryId = categoryEntity?.id,
        categoryName = categoryEntity?.name,
        productId = receiptDetailEntity.productId,
        unitId = unitEntity?.id,
        unitName = unitEntity?.name,
        amount = receiptDetailEntity.amount,
        discountAmount = receiptDetailEntity.discountAmount,
        discountPercent = receiptDetailEntity.discountPercent,
        exciseAmount = receiptDetailEntity.exciseAmount,
        exciseRateAmount = receiptDetailEntity.exciseRateAmount,
        marks = receiptDetailEntity.marks,
        vatAmount = receiptDetailEntity.vatAmount,
        vatPercent = receiptDetailEntity.vatPercent,
        quantity = receiptDetailEntity.quantity,
        price = receiptDetailEntity.price,
        packageType = null,
        status = ReceiptStatusResponse.valueOf(receiptDetailEntity.status),
        barcode = receiptDetailEntity.barcode,
        name = receiptDetailEntity.name,
        vatBarcode = receiptDetailEntity.vatBarcode,
        committentTin = receiptDetailEntity.committentTin,
        label = receiptDetailEntity.label
    )