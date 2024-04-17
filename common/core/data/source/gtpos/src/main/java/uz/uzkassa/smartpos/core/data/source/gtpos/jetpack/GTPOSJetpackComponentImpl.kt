package uz.uzkassa.smartpos.core.data.source.gtpos.jetpack

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import uz.uzkassa.smartpos.core.data.source.gtpos.observer.GTConnectObserver

internal class GTPOSJetpackComponentImpl(
    private val observer: GTConnectObserver
) : GTPOSJetpackComponent {

    override fun register(activity: ComponentActivity) =
        observer.register(activity)

    override fun register(fragment: Fragment) =
        observer.register(fragment)
}