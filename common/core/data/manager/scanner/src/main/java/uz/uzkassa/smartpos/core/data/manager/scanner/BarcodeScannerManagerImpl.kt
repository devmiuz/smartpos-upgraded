package uz.uzkassa.smartpos.core.data.manager.scanner

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.manager.scanner.params.BarcodeScannerParams
import uz.uzkassa.smartpos.core.data.manager.scanner.types.BarcodeScanner
import uz.uzkassa.smartpos.core.data.manager.scanner.types.hid.HIDBarcodeScanner

internal class BarcodeScannerManagerImpl : BarcodeScannerManager {
    private val scanner: BarcodeScanner = HIDBarcodeScanner()

    override fun getResult(): Flow<String> =
        scanner.getResult()

    override fun setBarcodeScannerParams(params: BarcodeScannerParams) {
        scanner.setBarcodeScannerParams(params)
    }
}