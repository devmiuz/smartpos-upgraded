package uz.uzkassa.smartpos.feature.user.saving.presentation.update.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.saving.data.repository.branch.BranchRepository
import uz.uzkassa.smartpos.feature.user.saving.data.repository.branch.BranchRepositoryImpl
import uz.uzkassa.smartpos.feature.user.saving.data.repository.user.UserRepository
import uz.uzkassa.smartpos.feature.user.saving.data.repository.user.UserRepositoryImpl
import uz.uzkassa.smartpos.feature.user.saving.data.repository.user.role.UserRoleRepository
import uz.uzkassa.smartpos.feature.user.saving.data.repository.user.role.UserRoleRepositoryImpl
import uz.uzkassa.smartpos.feature.user.saving.data.repository.user.saving.UserSavingRepository
import uz.uzkassa.smartpos.feature.user.saving.data.repository.user.saving.UserSavingRepositoryImpl

@Module(includes = [UserUpdateModule.Binders::class])
internal object UserUpdateModule {

    @Module
    interface Binders {

        @Binds
        @UserUpdateScope
        fun bindBranchRepository(
            impl: BranchRepositoryImpl
        ): BranchRepository

        @Binds
        @UserUpdateScope
        fun bindUserRepository(
            impl: UserRepositoryImpl
        ): UserRepository

        @Binds
        @UserUpdateScope
        fun bindUserRoleRepository(
            impl: UserRoleRepositoryImpl
        ): UserRoleRepository

        @Binds
        @UserUpdateScope
        fun bindUserSavingRepository(
            impl: UserSavingRepositoryImpl
        ): UserSavingRepository
    }
}