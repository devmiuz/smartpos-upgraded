package uz.uzkassa.smartpos.core.data.manager.scanner

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.manager.scanner.params.BarcodeScannerParams

interface BarcodeScannerManager {

    fun getResult(): Flow<String>

    fun setBarcodeScannerParams(params: BarcodeScannerParams)

    companion object {

        fun instantiate(): BarcodeScannerManager =
            BarcodeScannerManagerImpl()
    }
}