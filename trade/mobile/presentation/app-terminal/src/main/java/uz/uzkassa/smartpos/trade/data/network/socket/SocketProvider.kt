package uz.uzkassa.smartpos.trade.data.network.socket

import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.source.resource.apay.remote.socket.ApaySocketService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.socket.ReceiptSocketService
import uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.ScarletInstance

interface SocketProvider {

//    val receiptSocketService: ReceiptSocketService
    
    val apaySocketService: ApaySocketService

    companion object {

        fun instantiate(
            deviceInfoManager: DeviceInfoManager,
            scarletInstance: ScarletInstance
        ): SocketProvider = SocketProviderImpl(
            deviceInfoManager = deviceInfoManager,
            scarletInstance = scarletInstance
        )

    }
}