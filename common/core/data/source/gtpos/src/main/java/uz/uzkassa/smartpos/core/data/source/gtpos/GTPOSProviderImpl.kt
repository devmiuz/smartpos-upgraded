package uz.uzkassa.smartpos.core.data.source.gtpos

import android.content.Context
import uz.uzkassa.smartpos.core.data.source.gtpos.intent.GTPOSLaunchIntentImpl
import uz.uzkassa.smartpos.core.data.source.gtpos.jetpack.GTPOSJetpackComponent
import uz.uzkassa.smartpos.core.data.source.gtpos.jetpack.GTPOSJetpackComponentImpl
import uz.uzkassa.smartpos.core.data.source.gtpos.observer.GTConnectObserver
import uz.uzkassa.smartpos.core.data.source.gtpos.source.GTPOSSourceProvider
import uz.uzkassa.smartpos.core.data.source.gtpos.source.GTPOSSourceProviderImpl

internal class GTPOSProviderImpl(context: Context) : GTPOSProvider {
    private val observer: GTConnectObserver by lazy {
        GTConnectObserver(launchIntent)
    }

    override val launchIntent: GTPOSLaunchIntentImpl by lazy {
        GTPOSLaunchIntentImpl(context)
    }

    override val jetpackComponent: GTPOSJetpackComponent by lazy {
        GTPOSJetpackComponentImpl(observer)
    }

    override val sourceProvider: GTPOSSourceProvider by lazy {
        GTPOSSourceProviderImpl(observer)
    }
}