package uz.uzkassa.smartpos.core.data.source.gtpos.intent

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import uz.uzkassa.smartpos.core.data.source.gtpos.exception.GTPOSErrorType
import uz.uzkassa.smartpos.core.data.source.gtpos.exception.GTPOSException

internal class GTPOSLaunchIntentImpl(context: Context) : GTPOSLaunchIntent {
    private val packageManager = context.packageManager

    override fun intentOrThrow(type: GTPOSLaunchIntent.Type): Intent {
        val intent: Intent =
            packageManager.getLaunchIntentForPackage(PACKAGE_NAME)
                ?: throw GTPOSException(GTPOSErrorType.APP_NOT_DEFINED)

        val packageInfo = packageManager.getPackageInfo(PACKAGE_NAME, 0)
        if (packageInfo.versionCodeCompat < ALLOWED_GTPOS_VERSION_CODE)
            throw GTPOSException(GTPOSErrorType.UPDATE_REQUIRED)

        val className: String = when (type) {
            GTPOSLaunchIntent.Type.USER -> LAUNCH_GTPOS_INTENT_USER
            else -> checkNotNull(intent.component).className
        }

        return Intent().apply { setClassName(PACKAGE_NAME, className) }
    }

    @Suppress("DEPRECATION")
    private val PackageInfo.versionCodeCompat: Long
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) longVersionCode
            else kotlin.runCatching { versionCode.toLong() }.getOrDefault(0)
        }

    companion object {
        const val ALLOWED_GTPOS_VERSION_CODE = 8020
        const val LAUNCH_GTPOS_INTENT_USER: String = "by.a3c.gtpos.view.GTAppUser"
        const val PACKAGE_NAME: String = "by.a3c.gtpos"
    }
}