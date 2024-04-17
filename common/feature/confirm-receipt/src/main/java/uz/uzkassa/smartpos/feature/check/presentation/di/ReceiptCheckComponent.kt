package uz.uzkassa.smartpos.feature.check.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.check.dependencies.ReceiptCheckFeatureDependencies
import uz.uzkassa.smartpos.feature.check.domain.ReceiptCheckInteractor
import uz.uzkassa.smartpos.feature.check.presentation.ReceiptCheckFragment

@ReceiptCheckScope
@Component(
    dependencies = [ReceiptCheckFeatureDependencies::class],
    modules = [ReceiptCheckModule::class]
)
abstract class ReceiptCheckComponent {


//    internal abstract val receiptEntityDao: ReceiptEntityDao
//
//    internal abstract val receiptRestService: ReceiptRestService
//
//    internal abstract val coroutineContextManager:CoroutineContextManager
//
//    internal abstract val receiptCheckInteractor: ReceiptCheckInteractor

    internal abstract fun inject(fragment: ReceiptCheckFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: ReceiptCheckFeatureDependencies
        ): ReceiptCheckComponent
    }

    internal companion object {

        fun create(
            dependencies: ReceiptCheckFeatureDependencies
        ): ReceiptCheckComponent =
            DaggerReceiptCheckComponent
                .factory()
                .create(dependencies)
    }
}