package uz.uzkassa.smartpos.core.data.source.gtpos.jetpack

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment

interface GTPOSJetpackComponent {

    fun register(activity: ComponentActivity)

    fun register(fragment: Fragment)
}