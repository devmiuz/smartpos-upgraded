package uz.uzkassa.smartpos.feature.branch.selection.setup.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.channel.BranchSelectionBroadcastChannel
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.model.BranchSelection
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.repository.BranchRepository
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.repository.BranchRepositoryImpl
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.repository.selection.BranchSelectionRepository
import uz.uzkassa.smartpos.feature.branch.selection.setup.data.repository.selection.BranchSelectionRepositoryImpl

@Module(
    includes = [
        BranchSelectionSetupModule.Binders::class,
        BranchSelectionSetupModule.Providers::class
    ]
)
internal object BranchSelectionSetupModule {

    @Module
    interface Binders {

        @Binds
        @BranchSelectionSetupScope
        fun bindBranchRepository(
            impl: BranchRepositoryImpl
        ): BranchRepository

        @Binds
        @BranchSelectionSetupScope
        fun bindBranchSelectionRepository(
            impl: BranchSelectionRepositoryImpl
        ): BranchSelectionRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @BranchSelectionSetupScope
        fun provideBranchSelectionBroadcastChannel(): BranchSelectionBroadcastChannel =
            BranchSelectionBroadcastChannel()

        @JvmStatic
        @Provides
        @BranchSelectionSetupScope
        @FlowPreview
        fun provideBranchSelectionFlow(
            branchSelectionBroadcastChannel: BranchSelectionBroadcastChannel
        ): Flow<BranchSelection> =
            branchSelectionBroadcastChannel.asFlow()
    }
}