package uz.uzkassa.smartpos.feature.receipt.remote.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.receipt.remote.dependencies.ReceiptRemoteFeatureDependencies
import uz.uzkassa.smartpos.feature.receipt.remote.presentation.ReceiptRemoteLifecycleDelegate

@ReceiptRemoteScope
@Component(
    dependencies = [ReceiptRemoteFeatureDependencies::class],
    modules = [ReceiptRemoteModule::class]
)
abstract class ReceiptRemoteComponent : ReceiptRemoteFeatureDependencies {

    internal abstract fun inject(delegate: ReceiptRemoteLifecycleDelegate)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: ReceiptRemoteFeatureDependencies
        ): ReceiptRemoteComponent
    }

    internal companion object {

        fun create(
            dependencies: ReceiptRemoteFeatureDependencies
        ): ReceiptRemoteComponent =
            DaggerReceiptRemoteComponent
                .factory()
                .create(dependencies)
    }
}