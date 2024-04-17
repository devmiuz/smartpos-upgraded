package uz.uzkassa.smartpos.core.data.source.gtpos

import android.content.Context
import uz.uzkassa.smartpos.core.data.source.gtpos.intent.GTPOSLaunchIntent
import uz.uzkassa.smartpos.core.data.source.gtpos.jetpack.GTPOSJetpackComponent
import uz.uzkassa.smartpos.core.data.source.gtpos.source.GTPOSSourceProvider

interface GTPOSProvider {

    val launchIntent: GTPOSLaunchIntent

    val jetpackComponent: GTPOSJetpackComponent

    val sourceProvider: GTPOSSourceProvider

    companion object {

        fun instantiate(context: Context): GTPOSProvider =
            GTPOSProviderImpl(context)
    }
}