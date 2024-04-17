package uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.mapper

import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessType
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessTypeEntity
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessTypeResponse

fun List<CompanyBusinessTypeResponse>.map() =
    map { it.map() }

fun List<CompanyBusinessTypeResponse>.mapToEntities() =
    map { it.mapToEntity() }

fun CompanyBusinessTypeResponse.map() =
    CompanyBusinessType(
        code = code,
        nameRu = nameRu
    )

fun CompanyBusinessTypeResponse.mapToEntity() =
    CompanyBusinessTypeEntity(
        code = code,
        nameRu = nameRu
    )