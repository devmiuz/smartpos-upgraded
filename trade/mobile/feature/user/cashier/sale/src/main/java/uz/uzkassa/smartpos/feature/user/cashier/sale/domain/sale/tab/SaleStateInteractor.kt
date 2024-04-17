package uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sale.tab

import uz.uzkassa.smartpos.core.data.manager.printer.exception.PrinterException
import uz.uzkassa.smartpos.core.data.source.gtpos.exception.GTPOSException
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.SaleInteractor
import javax.inject.Inject

internal class SaleStateInteractor @Inject constructor(
    private val saleInteractor: SaleInteractor
) {
    private var isCloseShiftError: Boolean = false

    fun setCloseShiftError(throwable: Throwable) {
        isCloseShiftError = throwable is GTPOSException || throwable is PrinterException
    }

    val isExitFromSaleAllowed: Boolean
        get() = !isCloseShiftError || !saleInteractor.isPaymentReceived
}