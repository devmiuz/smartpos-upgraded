package uz.uzkassa.smartpos.feature.camera.presentation.delegate.format

import com.google.zxing.BarcodeFormat

@Suppress("SpellCheckingInspection")
enum class DecodeFormat(internal val barcodeFormat: BarcodeFormat) {
    AZTEC(BarcodeFormat.AZTEC),
    CODABAR(BarcodeFormat.CODABAR),
    CODE_39(BarcodeFormat.CODE_39),
    CODE_93(BarcodeFormat.CODE_39),
    CODE_128(BarcodeFormat.CODE_128),
    DATA_MATRIX(BarcodeFormat.DATA_MATRIX),
    EAN_8(BarcodeFormat.EAN_8),
    EAN_13(BarcodeFormat.EAN_13),
    ITF(BarcodeFormat.ITF),
    MAXICODE(BarcodeFormat.MAXICODE),
    PDF_417(BarcodeFormat.PDF_417),
    QR_CODE(BarcodeFormat.QR_CODE),
    RSS_14(BarcodeFormat.RSS_14),
    RSS_EXPANDED(BarcodeFormat.RSS_EXPANDED),
    UPC_A(BarcodeFormat.UPC_A),
    UPC_E(BarcodeFormat.UPC_E),
    UPC_EAN_EXTENSION(BarcodeFormat.UPC_EAN_EXTENSION)
}