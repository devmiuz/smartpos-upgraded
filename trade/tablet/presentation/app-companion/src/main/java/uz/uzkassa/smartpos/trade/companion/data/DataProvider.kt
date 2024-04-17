package uz.uzkassa.smartpos.trade.companion.data

import uz.uzkassa.smartpos.trade.companion.data.database.DatabaseProvider
import uz.uzkassa.smartpos.trade.companion.data.network.rest.RestProvider
import uz.uzkassa.smartpos.trade.companion.data.preference.PreferenceProvider

interface DataProvider :
    DatabaseProvider,
    PreferenceProvider,
    RestProvider
