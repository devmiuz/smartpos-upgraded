package uz.uzkassa.smartpos.core.data.source.resource.receipt.template.mapper

import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.ReceiptTemplateResponse
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.ReceiptTemplate
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.ReceiptTemplateEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.alignment.ReceiptTemplateAlignment

fun List<ReceiptTemplateResponse>.mapToEntities() =
    map { it.mapToEntity() }

fun ReceiptTemplateEntity.map() =
    ReceiptTemplate(
        id = id,
        companyId = companyId,
        footerAlignment = footerAlignment?.let { ReceiptTemplateAlignment.valueOf(it) },
        footerImage = footerImage,
        footerImageContentType = footerImageContentType,
        footerText = footerText,
        headerAlignment = headerAlignment?.let { ReceiptTemplateAlignment.valueOf(it) },
        headerImage = headerImage,
        headerImageContentType = headerImageContentType,
        headerText = headerText
    )

fun ReceiptTemplateResponse.map() =
    ReceiptTemplate(
        id = id,
        companyId = companyId,
        footerAlignment = footerAlignment?.let { ReceiptTemplateAlignment.valueOf(it.name) },
        footerImage = footerImage,
        footerImageContentType = footerImageContentType,
        footerText = footerText,
        headerAlignment = headerAlignment?.let { ReceiptTemplateAlignment.valueOf(it.name) },
        headerImage = headerImage,
        headerImageContentType = headerImageContentType,
        headerText = headerText
    )

fun ReceiptTemplateResponse.mapToEntity() =
    ReceiptTemplateEntity(
        id = id,
        companyId = companyId,
        footerAlignment = footerAlignment?.name,
        footerImage = footerImage,
        footerImageContentType = footerImageContentType,
        footerText = footerText,
        headerAlignment = headerAlignment?.name,
        headerImage = headerImage,
        headerImageContentType = headerImageContentType,
        headerText = headerText
    )