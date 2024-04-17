package uz.uzkassa.smartpos.trade.companion.presentation.app.di.data

import dagger.Module
import uz.uzkassa.smartpos.trade.companion.presentation.app.di.data.modules.ApplicationDataModuleDatabase
import uz.uzkassa.smartpos.trade.companion.presentation.app.di.data.modules.ApplicationDataModuleNetwork
import uz.uzkassa.smartpos.trade.companion.presentation.app.di.data.modules.ApplicationDataModulePreference
import uz.uzkassa.smartpos.trade.companion.presentation.app.di.data.modules.ApplicationDataModuleRest

@Module(
    includes = [
        ApplicationDataModuleDatabase::class,
        ApplicationDataModuleNetwork::class,
        ApplicationDataModulePreference::class,
        ApplicationDataModuleRest::class
    ]
)
object ApplicationModuleData