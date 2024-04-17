package uz.uzkassa.smartpos.core.data.source.resource.company.vat.mapper

import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVAT
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVATResponse

fun List<CompanyVATResponse>.map() =
    map { it.map() }

fun CompanyVATResponse.map() =
    CompanyVAT(
        id = id,
        name = name,
        percent = percent
    )