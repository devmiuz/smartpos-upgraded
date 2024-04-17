package uz.uzkassa.smartpos.feature.branch.delete.presentation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.branch.delete.data.repository.BranchRepository
import uz.uzkassa.smartpos.feature.branch.delete.data.repository.BranchRepositoryImpl
import uz.uzkassa.smartpos.feature.branch.delete.data.repository.delete.BranchDeletionRepository
import uz.uzkassa.smartpos.feature.branch.delete.data.repository.delete.BranchDeletionRepositoryImpl

@Module(includes = [BranchDeleteModule.Binders::class])
internal object BranchDeleteModule {

    @Module
    interface Binders {

        @Binds
        @BranchDeleteScope
        fun bindBranchRepository(
            impl: BranchRepositoryImpl
        ): BranchRepository

        @Binds
        @BranchDeleteScope
        fun bindBranchDeletionRepository(
            impl: BranchDeletionRepositoryImpl
        ): BranchDeletionRepository
    }
}