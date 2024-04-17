package uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.mapper

import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessType
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessTypeEntity

fun List<CompanyBusinessTypeEntity>.map() =
    map { it.map() }

fun CompanyBusinessTypeEntity.map() =
    CompanyBusinessType(
        code = code,
        nameRu = nameRu
    )