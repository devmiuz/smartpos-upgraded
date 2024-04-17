package uz.uzkassa.smartpos.feature.check.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.check.data.repository.ReceiptCheckRepository
import uz.uzkassa.smartpos.feature.check.data.repository.ReceiptCheckRepositoryImpl
import uz.uzkassa.smartpos.feature.check.dependencies.ReceiptCheckFeatureArgs
import uz.uzkassa.smartpos.feature.check.dependencies.ReceiptCheckFeatureDependencies
import uz.uzkassa.smartpos.feature.check.domain.ReceiptCheckInteractor

@Module(includes = [ReceiptCheckModule.Binders::class, ReceiptCheckModule.Providers::class])
internal object ReceiptCheckModule {

    @Module
    interface Binders {

        @Binds
        @ReceiptCheckScope
        fun bindReceiptCheckRepository(
            impl: ReceiptCheckRepositoryImpl
        ): ReceiptCheckRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @ReceiptCheckScope
        @FlowPreview
        fun provideQrScannerFlow(
            receiptCheckFeatureArgs: ReceiptCheckFeatureArgs
        ): Flow<String> =
            receiptCheckFeatureArgs.qrUrlBroadcastChannel.asFlow()

//        @JvmStatic
//        @Provides
//        @ReceiptCheckScope
//        fun provideReceiptCheckInteractor(
//            receiptCheckRepository: ReceiptCheckRepository,
//            dependencies: ReceiptCheckFeatureDependencies
//        ): ReceiptCheckInteractor =
//            ReceiptCheckInteractor(
//                receiptCheckRepository,
//                dependencies.receiptCheckFeatureArgs,
//                dependencies.coroutineContextManager,
//                dependencies.receiptEntityDao,
//                dependencies.receiptRestService
//            )
    }
}