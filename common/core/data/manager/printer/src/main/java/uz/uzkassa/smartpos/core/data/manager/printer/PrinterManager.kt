package uz.uzkassa.smartpos.core.data.manager.printer

import android.content.Context
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.manager.printer.print.PrintData

interface PrinterManager {

    fun print(data: PrintData): Flow<Unit>

    companion object {

        fun instantiate(context: Context): PrinterManager =
            PrinterManagerImpl(context)
    }
}