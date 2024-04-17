package uz.uzkassa.smartpos.core.data.manager.scanner.types

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.manager.scanner.params.BarcodeScannerParams

internal interface BarcodeScanner {

    fun isResultAvailable(): Boolean

    fun getResult(): Flow<String>

    fun setBarcodeScannerParams(params: BarcodeScannerParams): Boolean
}