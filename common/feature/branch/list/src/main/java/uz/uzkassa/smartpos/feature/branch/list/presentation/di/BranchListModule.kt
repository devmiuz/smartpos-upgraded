package uz.uzkassa.smartpos.feature.branch.list.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.feature.branch.list.data.model.confirmation.OwnerConfirmationState
import uz.uzkassa.smartpos.feature.branch.list.data.repository.BranchRepository
import uz.uzkassa.smartpos.feature.branch.list.data.repository.BranchRepositoryImpl
import uz.uzkassa.smartpos.feature.branch.list.dependencies.BranchListFeatureArgs

@Module(includes = [BranchListModule.Binders::class, BranchListModule.Providers::class])
internal object BranchListModule {

    @Module
    interface Binders {

        @Binds
        @BranchListScope
        fun bindBranchRepository(
            impl: BranchRepositoryImpl
        ): BranchRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @BranchListScope
        @FlowPreview
        fun provideUserAuthConfirmationStateFlow(
            branchListFeatureArgs: BranchListFeatureArgs
        ): Flow<OwnerConfirmationState> =
            branchListFeatureArgs.ownerConfirmationStateBroadcastChannel.asFlow()

    }
}