package uz.uzkassa.smartpos.core.data.manager.printer.print

import android.content.Context
import uz.uzkassa.smartpos.core.data.manager.printer.content.PrintContentData

interface PrintData {

    fun getPrintContentData(context: Context): List<PrintContentData>
}