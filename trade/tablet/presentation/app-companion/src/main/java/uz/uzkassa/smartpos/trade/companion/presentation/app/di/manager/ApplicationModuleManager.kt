package uz.uzkassa.smartpos.trade.companion.presentation.app.di.manager

import dagger.Module
import uz.uzkassa.smartpos.trade.companion.presentation.app.di.manager.modules.ApplicationModule
import uz.uzkassa.smartpos.trade.companion.presentation.app.di.manager.modules.ApplicationModuleCoroutine

@Module(
    includes = [
        ApplicationModuleCoroutine::class,
        ApplicationModule::class
    ]
)
object ApplicationModuleManager