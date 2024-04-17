package uz.uzkassa.smartpos.trade.data.network.socket

import com.tinder.scarlet.Protocol
import com.tinder.scarlet.Scarlet
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.source.resource.apay.remote.socket.ApaySocketService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.remote.socket.ReceiptSocketService
import uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.ScarletInstance
import uz.uzkassa.smartpos.trade.data.network.socket.impl.scarlet.okhttp.client.OkHttpStompDestination

class SocketProviderImpl(
    private val deviceInfoManager: DeviceInfoManager,
    private val scarletInstance: ScarletInstance
) : SocketProvider {

    private val configuration: Scarlet.Configuration
        get() = scarletInstance.scarletConfiguration

    private val protocol: (destination: String) -> Protocol
        get() = {
            if (it.isEmpty()) scarletInstance.protocol
            else OkHttpStompDestination(it)
        }

    private val scarlet: Scarlet
        get() = scarletInstance.scarlet

//    override val receiptSocketService: ReceiptSocketService by lazy {
//        ReceiptSocketService.instantiate(deviceInfoManager, scarlet, configuration, protocol)
//    }

    override val apaySocketService: ApaySocketService by lazy {
        ApaySocketService.instantiate(deviceInfoManager, scarlet, configuration, protocol)
    }

}