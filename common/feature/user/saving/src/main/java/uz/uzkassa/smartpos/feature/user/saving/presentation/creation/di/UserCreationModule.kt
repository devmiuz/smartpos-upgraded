package uz.uzkassa.smartpos.feature.user.saving.presentation.creation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.saving.data.repository.branch.BranchRepository
import uz.uzkassa.smartpos.feature.user.saving.data.repository.branch.BranchRepositoryImpl
import uz.uzkassa.smartpos.feature.user.saving.data.repository.user.role.UserRoleRepository
import uz.uzkassa.smartpos.feature.user.saving.data.repository.user.role.UserRoleRepositoryImpl
import uz.uzkassa.smartpos.feature.user.saving.data.repository.user.saving.UserSavingRepository
import uz.uzkassa.smartpos.feature.user.saving.data.repository.user.saving.UserSavingRepositoryImpl

@Module(includes = [UserCreationModule.Binders::class])
internal object UserCreationModule {

    @Module
    interface Binders {

        @Binds
        @UserCreationScope
        fun bindBranchRepository(
            impl: BranchRepositoryImpl
        ): BranchRepository

        @Binds
        @UserCreationScope
        fun bindUserRoleRepository(
            impl: UserRoleRepositoryImpl
        ): UserRoleRepository

        @Binds
        @UserCreationScope
        fun bindUserSavingRepository(
            impl: UserSavingRepositoryImpl
        ): UserSavingRepository
    }
}