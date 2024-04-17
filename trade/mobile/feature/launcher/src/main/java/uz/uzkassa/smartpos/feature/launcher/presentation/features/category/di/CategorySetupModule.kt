package uz.uzkassa.smartpos.feature.launcher.presentation.features.category.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.launcher.data.repository.branch.CurrentBranchRepository
import uz.uzkassa.smartpos.feature.launcher.data.repository.branch.CurrentBranchRepositoryImpl

@Module(includes = [CategorySetupModule.Binders::class])
internal object CategorySetupModule {

    @Module
    interface Binders {

        @Binds
        @CategorySetupScope
        fun bindCurrentBranchRepository(
            impl: CurrentBranchRepositoryImpl
        ): CurrentBranchRepository
    }
}