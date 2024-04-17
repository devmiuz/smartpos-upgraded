package uz.uzkassa.smartpos.core.data.manager.printer.content

data class TextPairPrintContentData(
    val leftText: String,
    val rightText: String,
    val fontSize: Int = 20,
    val isBold: Boolean = false
) : PrintContentData
