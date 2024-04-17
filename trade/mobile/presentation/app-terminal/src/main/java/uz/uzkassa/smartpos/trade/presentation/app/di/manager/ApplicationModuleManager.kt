package uz.uzkassa.smartpos.trade.presentation.app.di.manager

import dagger.Module
import uz.uzkassa.smartpos.trade.presentation.app.di.manager.modules.*

@Module(
    includes = [
        ApplicationModuleCoroutine::class,
        ApplicationModuleDeviceInfo::class,
        ApplicationModule::class,
        ApplicationModulePrinter::class,
        ApplicationModuleScanner::class
    ]
)
object ApplicationModuleManager