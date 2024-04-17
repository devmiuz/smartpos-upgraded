package uz.uzkassa.smartpos.trade.manager.printer

import android.content.Context
import uz.uzkassa.smartpos.core.data.manager.printer.PrinterManager

interface PrinterManagerProvider {
    
    val printerManager: PrinterManager

    companion object {

        fun instantiate(context: Context): PrinterManagerProvider =
            PrinterManagerProviderImpl(context)
    }
}