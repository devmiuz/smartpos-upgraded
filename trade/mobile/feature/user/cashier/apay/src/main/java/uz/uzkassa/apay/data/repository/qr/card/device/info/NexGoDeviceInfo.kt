package uz.uzkassa.apay.data.repository.qr.card.device.info

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build.MODEL
import com.nexgo.oaf.apiv3.APIProxy
import com.nexgo.oaf.apiv3.DeviceEngine
import com.nexgo.oaf.apiv3.device.reader.CardSlotTypeEnum
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale
import javax.inject.Inject

internal class NexGoDeviceInfo constructor(context: Context) : DeviceInfo {
    private val deviceEngine: DeviceEngine by lazy { APIProxy.getDeviceEngine(context) }
    override val deviceModel: String = MODEL
    override val serialNumber: String by lazy { deviceEngine.deviceInfo.sn }

    @SuppressLint("WeekBasedYear")
    override fun updateTime(date: Date) {
        deviceEngine.setSystemClock(SimpleDateFormat("yyyyMMddHHmmss", Locale.ROOT).format(date))
    }

    override fun isCardSlotBusy(): Flow<Boolean> {
        return flowOf(deviceEngine.cardReader.isCardExist(CardSlotTypeEnum.ICC1))
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    override fun uninstallApp(packageName: String): Flow<Unit> =
        callbackFlow {
            deviceEngine.uninstallApp(packageName) {
                offer(Unit)
                close()
            }
            awaitClose { cancel() }
        }

    companion object : DeviceInfo.Availability {

        override val isAvailable: Boolean =
            arrayOf("N3", "N5", "N7", "N86", "P100").contains(MODEL.toUpperCase(Locale.ROOT))
    }
}