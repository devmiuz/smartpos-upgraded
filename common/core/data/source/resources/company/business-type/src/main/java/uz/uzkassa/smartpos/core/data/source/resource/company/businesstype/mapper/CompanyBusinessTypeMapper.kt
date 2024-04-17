package uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.mapper

import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessType
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessTypeResponse

fun CompanyBusinessType.mapToResponse() =
    CompanyBusinessTypeResponse(
        code = code,
        nameRu = nameRu
    )