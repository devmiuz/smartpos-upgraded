package uz.uzkassa.smartpos.feature.check.dependencies

interface ReceiptCheckFeatureCallback {
    fun onBackFromCheckReceipt()
    fun openQrScannerScreen()
    fun backToReceiptCheckScreen(url: String)
}