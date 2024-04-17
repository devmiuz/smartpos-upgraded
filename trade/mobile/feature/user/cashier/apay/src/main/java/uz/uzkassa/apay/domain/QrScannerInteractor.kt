package uz.uzkassa.apay.domain

import uz.uzkassa.apay.data.repository.qr.scanner.QrScannerRepository
import uz.uzkassa.apay.presentation.navigation.CashierApayRouter
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import javax.inject.Inject

internal class QrScannerInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val qrScannerRepository: QrScannerRepository,
    private val cashierApayRouter: CashierApayRouter
) {

}