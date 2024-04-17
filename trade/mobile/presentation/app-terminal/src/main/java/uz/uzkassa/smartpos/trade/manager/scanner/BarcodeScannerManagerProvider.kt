package uz.uzkassa.smartpos.trade.manager.scanner

import uz.uzkassa.smartpos.core.data.manager.scanner.BarcodeScannerManager

interface BarcodeScannerManagerProvider {

    val barcodeScannerManager: BarcodeScannerManager

    companion object {

        fun instantiate(): BarcodeScannerManagerProvider =
            BarcodeScannerManagerProviderImpl()
    }
}