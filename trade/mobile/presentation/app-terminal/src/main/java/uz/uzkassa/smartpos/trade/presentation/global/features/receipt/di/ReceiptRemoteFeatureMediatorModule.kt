package uz.uzkassa.smartpos.trade.presentation.global.features.receipt.di

import androidx.activity.ComponentActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.feature.receipt.remote.dependencies.ReceiptRemoteFeatureArgs
import uz.uzkassa.smartpos.feature.receipt.remote.presentation.ReceiptRemoteLifecycleDelegate
import uz.uzkassa.smartpos.trade.presentation.global.di.GlobalScope
import uz.uzkassa.smartpos.trade.presentation.global.features.receipt.ReceiptRemoteFeatureMediator

@Module(
    includes = [
        ReceiptRemoteFeatureMediatorModule.Binders::class,
        ReceiptRemoteFeatureMediatorModule.Providers::class
    ]
)
object ReceiptRemoteFeatureMediatorModule {

    @Module
    interface Binders {

        @Binds
        @GlobalScope
        fun bindReceiptRemoteFeatureArgs(
            mediator: ReceiptRemoteFeatureMediator
        ): ReceiptRemoteFeatureArgs
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideReceiptRemoteFeatureMediator(): ReceiptRemoteFeatureMediator =
            ReceiptRemoteFeatureMediator()

        @JvmStatic
        @Provides
        @GlobalScope
        fun provideReceiptRemoteLifecycleDelegate(
            activity: ComponentActivity,
            mediator: ReceiptRemoteFeatureMediator
        ): ReceiptRemoteLifecycleDelegate =
            mediator.getReceiptRemoteLifecycleDelegate(activity)
    }
}