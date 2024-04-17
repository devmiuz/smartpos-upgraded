package uz.uzkassa.smartpos.trade.manager.scanner

import uz.uzkassa.smartpos.core.data.manager.scanner.BarcodeScannerManager

class BarcodeScannerManagerProviderImpl : BarcodeScannerManagerProvider {

    override val barcodeScannerManager by lazy {
        BarcodeScannerManager.instantiate()
    }
}