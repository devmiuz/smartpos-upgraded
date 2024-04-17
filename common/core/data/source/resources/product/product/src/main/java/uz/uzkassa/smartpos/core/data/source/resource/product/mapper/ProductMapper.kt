package uz.uzkassa.smartpos.core.data.source.resource.product.mapper

import uz.uzkassa.smartpos.core.data.source.resource.category.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.product.model.Product
import uz.uzkassa.smartpos.core.data.source.resource.product.model.ProductEntity
import uz.uzkassa.smartpos.core.data.source.resource.product.model.ProductRelation
import uz.uzkassa.smartpos.core.data.source.resource.product.model.ProductResponse
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.unit.mapper.map
import java.math.BigDecimal

fun List<ProductRelation>.map() =
    map { it.map() }

fun List<ProductResponse>.mapToEntities() =
    map { it.mapToEntity() }

fun ProductRelation.map(): Product =
    Product(
        id = productEntity.id,
        branchId = productEntity.branchId,
        isCustom = productEntity.isCustom,
        isDeleted = productEntity.isDeleted,
        isFavorite = productEntity.isFavorite,
        isNoVAT = productEntity.isNoVat,
        hasExcise = productEntity.hasExcise,
        hasMark = productEntity.hasMark,
        barcode = productEntity.barcode,
        vatBarcode = productEntity.vatBarcode,
        code = productEntity.code,
        model = productEntity.model,
        measurement = productEntity.measurement,
        count = productEntity.count ?: 0.0,
        exciseAmount = productEntity.exciseAmount,
        salesPrice = productEntity.salesPrice ?: BigDecimal.ZERO,
        vatRate = productEntity.vatRate,
        name = productEntity.name,
        description = productEntity.description,
        category = categoryEntity?.map(),
        unit = unitEntity?.map(),
        productUnits = productUnitRelations?.map(),
        commintentTin = productEntity.committentTin,
        vatPercent = productEntity.vatPercent,
        unitId = productEntity.unitId,
        label = productEntity.label
    )

fun ProductResponse.mapToEntity() =
    ProductEntity(
        id = id,
        categoryId = category.id,
        packageTypeId = null,
        unitId = unit?.id,
        productUnitIds = productUnits?.mapNotNull { it.id }?.toLongArray(),
        branchId = branchId,
        isCustom = isCustom,
        isDeleted = isDeleted ?: false,
        isFavorite = isFavorite ?: false,
        isNoVat = isNoVat ?: false,
        hasExcise = hasExcise,
        hasMark = hasMark ?: false,
        barcode = barcode ?: "",
        code = code,
        model = model,
        measurement = measurement,
        count = count ?: 0.0,
        exciseAmount = exciseAmount,
        salesPrice = salesPrice,
        vatRate = vatRate,
        name = name ?: id.toString(),
        nameUz = nameUz,
        description = description,
        createdBy = createdBy,
        createdDate = createdDate,
        lastModifiedBy = lastModifiedBy,
        lastModifiedDate = lastModifiedDate,
        isService = isService,
        vatBarcode = vatBarcode,
        committentTin = committentTin,
        vatPercent = vatPercent,
        label = label
    )