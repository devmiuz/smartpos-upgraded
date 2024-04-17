package uz.uzkassa.smartpos.trade.presentation.global.features.receipt

import androidx.activity.ComponentActivity
import uz.uzkassa.smartpos.feature.receipt.remote.dependencies.ReceiptRemoteFeatureArgs
import uz.uzkassa.smartpos.feature.receipt.remote.presentation.ReceiptRemoteLifecycleDelegate
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator

class ReceiptRemoteFeatureMediator : FeatureMediator, ReceiptRemoteFeatureArgs {

    fun getReceiptRemoteLifecycleDelegate(
        activity: ComponentActivity
    ): ReceiptRemoteLifecycleDelegate =
        ReceiptRemoteLifecycleDelegate(activity, activity)
}