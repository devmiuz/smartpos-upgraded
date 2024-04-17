package uz.uzkassa.smartpos.feature.launcher.presentation.features.user.creation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.launcher.data.repository.branch.CurrentBranchRepository
import uz.uzkassa.smartpos.feature.launcher.data.repository.branch.CurrentBranchRepositoryImpl
import uz.uzkassa.smartpos.feature.launcher.data.repository.company.CurrentCompanyRepository
import uz.uzkassa.smartpos.feature.launcher.data.repository.company.CurrentCompanyRepositoryImpl

@Module(includes = [UserCreationModule.Binders::class])
internal object UserCreationModule {

    @Module
    interface Binders {

        @Binds
        @UserCreationScope
        fun bindCurrentBranchRepository(
            impl: CurrentBranchRepositoryImpl
        ): CurrentBranchRepository

        @Binds
        @UserCreationScope
        fun bindCurrentCompanyRepository(
            impl: CurrentCompanyRepositoryImpl
        ): CurrentCompanyRepository
    }
}