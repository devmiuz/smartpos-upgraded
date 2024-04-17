package uz.uzkassa.smartpos.core.data.manager.printer.content

import uz.uzkassa.smartpos.core.data.manager.printer.content.alignment.TextAlignmentPrintParams

class TextSinglePrintContentData(
    val text: String,
    val fontSize: Int = 20,
    val alignmentParams: TextAlignmentPrintParams = TextAlignmentPrintParams.CENTER,
    val isBold: Boolean = false
) : PrintContentData {

    constructor(
        text: String,
        alignmentParams: TextAlignmentPrintParams
    ) : this(
        text = text,
        fontSize = 20,
        alignmentParams = alignmentParams,
        isBold = false
    )
}