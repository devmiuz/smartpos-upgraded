package uz.uzkassa.smartpos.trade.data

import uz.uzkassa.smartpos.trade.data.database.DatabaseProvider
import uz.uzkassa.smartpos.trade.data.fiscal.FiscalSourceProvider
import uz.uzkassa.smartpos.trade.data.gtpos.GTPOSProvider
import uz.uzkassa.smartpos.trade.data.network.rest.RestProvider
import uz.uzkassa.smartpos.trade.data.network.socket.SocketProvider
import uz.uzkassa.smartpos.trade.data.preference.PreferenceProvider
import uz.uzkassa.smartpos.trade.data.store.StoreProvider

interface DataProvider :
    DatabaseProvider,
    FiscalSourceProvider,
    GTPOSProvider,
    PreferenceProvider,
    RestProvider,
    SocketProvider,
    StoreProvider