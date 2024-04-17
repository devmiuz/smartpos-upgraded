package uz.uzkassa.smartpos.feature.branch.saving.presentation.creation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.feature.branch.saving.data.repository.saving.BranchSavingRepository
import uz.uzkassa.smartpos.feature.branch.saving.data.repository.saving.BranchSavingRepositoryImpl
import uz.uzkassa.smartpos.feature.branch.saving.dependencies.BranchSavingFeatureArgs

@Module(includes = [BranchCreationModule.Binders::class, BranchCreationModule.Providers::class])
internal object BranchCreationModule {

    @Module
    interface Binders {

        @Binds
        @BranchCreationScope
        fun bindBranchSavingRepository(
            impl: BranchSavingRepositoryImpl
        ): BranchSavingRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @BranchCreationScope
        @FlowPreview
        fun provideActivityTypeFlow(
            branchSavingFeatureArgs: BranchSavingFeatureArgs
        ): Flow<ActivityType> =
            branchSavingFeatureArgs.activityTypeBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @BranchCreationScope
        @FlowPreview
        fun provideRegionCityPairFlow(
            branchSavingFeatureArgs: BranchSavingFeatureArgs
        ): Flow<Pair<Region, City>> =
            branchSavingFeatureArgs.regionCityBroadcastChannel.asFlow()
    }
}