package uz.uzkassa.smartpos.trade.manager.printer

import android.content.Context
import uz.uzkassa.smartpos.core.data.manager.printer.PrinterManager

class PrinterManagerProviderImpl(context: Context) : PrinterManagerProvider {

    override val printerManager by lazy {
        PrinterManager.instantiate(context)
    }
}