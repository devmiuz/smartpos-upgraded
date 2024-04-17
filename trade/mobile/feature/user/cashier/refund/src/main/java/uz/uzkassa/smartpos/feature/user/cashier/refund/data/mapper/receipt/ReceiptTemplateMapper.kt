package uz.uzkassa.smartpos.feature.user.cashier.refund.data.mapper.receipt

import uz.uzkassa.smartpos.core.data.manager.printer.content.ImagePrintContentData
import uz.uzkassa.smartpos.core.data.manager.printer.content.PrintContentData
import uz.uzkassa.smartpos.core.data.manager.printer.content.TextSinglePrintContentData
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.ReceiptTemplate
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.model.alignment.ReceiptTemplateAlignment

fun ReceiptTemplate?.mapToHeaderPrintContentData(): List<PrintContentData> {
    if (this == null) return emptyList()
    val list: MutableList<PrintContentData> = arrayListOf()

    if (headerAlignment?.isTopAlignment == true)
        headerText?.let { list.add(TextSinglePrintContentData(it)) }

    headerImage?.let { list.add(ImagePrintContentData(it)) }

    if (headerAlignment?.isBottomAlignment == true)
        headerText?.let { list.add(TextSinglePrintContentData(it)) }

    return list
}

fun ReceiptTemplate?.mapToFooterPrintContentData(): List<PrintContentData> {
    if (this == null) return emptyList()
    val list: MutableList<PrintContentData> = arrayListOf()

    if (footerAlignment?.isTopAlignment == true)
        footerText?.let { list.add(TextSinglePrintContentData(it)) }

    footerImage?.let { list.add(ImagePrintContentData(it)) }

    if (footerAlignment?.isBottomAlignment == true)
        footerText?.let { list.add(TextSinglePrintContentData(it)) }

    return list
}

private val ReceiptTemplateAlignment?.isTopAlignment
    get() = this == ReceiptTemplateAlignment.LEFT || this == ReceiptTemplateAlignment.TOP

private val ReceiptTemplateAlignment?.isBottomAlignment
    get() = this == ReceiptTemplateAlignment.RIGHT || this == ReceiptTemplateAlignment.BOTTOM