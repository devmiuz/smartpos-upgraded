package uz.uzkassa.smartpos.trade.manager

import uz.uzkassa.smartpos.trade.manager.coroutine.CoroutineContextManagerProvider
import uz.uzkassa.smartpos.trade.manager.device.DeviceInfoManagerProvider
import uz.uzkassa.smartpos.trade.manager.printer.PrinterManagerProvider
import uz.uzkassa.smartpos.trade.manager.scanner.BarcodeScannerManagerProvider

interface ManagerProvider :
    BarcodeScannerManagerProvider,
    CoroutineContextManagerProvider,
    DeviceInfoManagerProvider,
    PrinterManagerProvider
