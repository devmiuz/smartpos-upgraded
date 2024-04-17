package uz.uzkassa.smartpos.trade.presentation.app.di.data

import dagger.Module
import uz.uzkassa.smartpos.trade.presentation.app.di.data.modules.*

@Module(
    includes = [
        ApplicationDataModuleDatabase::class,
        ApplicationDataModuleFiscal::class,
        ApplicationDataModuleGTPOS::class,
        ApplicationDataModuleNetwork::class,
        ApplicationDataModulePreference::class,
        ApplicationDataModuleRest::class,
        ApplicationDataModuleSocketService::class,
        ApplicationDataModuleStore::class
    ]
)
object ApplicationModuleData