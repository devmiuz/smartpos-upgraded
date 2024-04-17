package uz.uzkassa.smartpos.trade.data.network.utils.interceptor.details

import okhttp3.Request
import uz.uzkassa.smartpos.core.data.manager.device.DeviceInfoManager
import uz.uzkassa.smartpos.core.data.manager.device.model.DeviceInfo
import uz.uzkassa.smartpos.feature.launcher.data.preference.branch.CurrentBranchPreference
import uz.uzkassa.smartpos.trade.data.network.utils.okhttp.interceptor.request.HttpRequestInterceptor

internal class HeaderDetailsInterceptor(
    private val currentBranchPreference: CurrentBranchPreference,
    private val deviceInfoManager: DeviceInfoManager
) : HttpRequestInterceptor {
    private val deviceInfo: DeviceInfo by lazy { deviceInfoManager.deviceInfo }

    override fun intercept(request: Request): Request {
        return request.newBuilder()
            .apply {
                currentBranchPreference.branchId?.let { addHeader("branchId", it.toString()) }
                addHeader("TERMINAL_MODEL", deviceInfo.deviceName)
                addHeader("TERMINAL_SN", deviceInfo.serialNumber)
            }
            .header("Connection","close")
            .url(request.url)
            .build()
    }
}