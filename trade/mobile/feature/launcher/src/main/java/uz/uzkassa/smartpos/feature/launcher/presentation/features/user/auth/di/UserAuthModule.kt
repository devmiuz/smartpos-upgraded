package uz.uzkassa.smartpos.feature.launcher.presentation.features.user.auth.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.launcher.data.repository.branch.CurrentBranchRepository
import uz.uzkassa.smartpos.feature.launcher.data.repository.branch.CurrentBranchRepositoryImpl
import uz.uzkassa.smartpos.feature.launcher.data.repository.company.CurrentCompanyRepository
import uz.uzkassa.smartpos.feature.launcher.data.repository.company.CurrentCompanyRepositoryImpl
import uz.uzkassa.smartpos.feature.launcher.data.repository.users.UsersRepository
import uz.uzkassa.smartpos.feature.launcher.data.repository.users.UsersRepositoryImpl

@Module(includes = [UserAuthModule.Binders::class])
internal object UserAuthModule {

    @Module
    interface Binders {

        @Binds
        @UserAuthScope
        fun bindCurrentBranchRepository(
            impl: CurrentBranchRepositoryImpl
        ): CurrentBranchRepository

        @Binds
        @UserAuthScope
        fun bindCurrentCompanyRepository(
            impl: CurrentCompanyRepositoryImpl
        ): CurrentCompanyRepository

        @Binds
        @UserAuthScope
        fun bindUsersRepository(
            impl: UsersRepositoryImpl
        ): UsersRepository
    }
}