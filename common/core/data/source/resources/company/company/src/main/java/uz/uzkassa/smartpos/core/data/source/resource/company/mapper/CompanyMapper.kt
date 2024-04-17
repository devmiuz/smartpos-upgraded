package uz.uzkassa.smartpos.core.data.source.resource.company.mapper

import uz.uzkassa.smartpos.core.data.source.resource.activitytype.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.city.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.company.model.Company
import uz.uzkassa.smartpos.core.data.source.resource.company.model.CompanyEntity
import uz.uzkassa.smartpos.core.data.source.resource.company.model.CompanyRelation
import uz.uzkassa.smartpos.core.data.source.resource.company.model.CompanyResponse
import uz.uzkassa.smartpos.core.data.source.resource.region.mapper.map

fun CompanyRelation.map() =
    Company(
        id = companyEntity.id,
        createdDate = companyEntity.createdDate,
        activityType = activityTypeEntities.map(),
        companyBusinessType = companyBusinessTypeEntity.map(),
        city = cityEntity.map(),
        region = regionEntity.map(),
        isWarehouseEnabled = companyEntity.isWarehouseEnabled,
        isPaysVat = companyEntity.isPaysVat,
        isFiscal = companyEntity.isFiscal,
        vatPercent = companyEntity.vatPercent,
        tin = companyEntity.tin,
        name = companyEntity.name,
        address = companyEntity.address,
        paymentTypes = companyEntity.paymentTypes
    )

fun CompanyResponse.map() =
    Company(
        id = id,
        createdDate = createdDate,
        activityType = activityType.map(),
        companyBusinessType = companyBusinessType.map(),
        city = city.map(),
        region = region.map(),
        isWarehouseEnabled = isWarehouseEnabled,
        isPaysVat = isPaysVat,
        vatPercent = vatPercent,
        isFiscal = isFiscal,
        tin = tin,
        name = name,
        address = address,
        paymentTypes = paymentTypes
    )

fun CompanyResponse.mapToEntity() =
    CompanyEntity(
        id = id,
        createdDate = createdDate,
        activityTypeIds = activityType.map { it.id }.toLongArray(),
        companyBusinessTypeCode = companyBusinessType.code,
        cityId = city.id,
        regionId = region.id,
        isWarehouseEnabled = isWarehouseEnabled,
        isPaysVat = isPaysVat,
        isFiscal = isFiscal,
        vatPercent = vatPercent,
        tin = tin,
        name = name,
        address = address,
        paymentTypes = paymentTypes
    )