package uz.uzkassa.smartpos.feature.user.list.presentation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.user.list.data.UserRepository
import uz.uzkassa.smartpos.feature.user.list.data.UserRepositoryImpl
import uz.uzkassa.smartpos.feature.user.list.data.delete.UserDeleteRepository
import uz.uzkassa.smartpos.feature.user.list.data.delete.UserDeleteRepositoryImpl

@Module(includes = [UserListModule.Binders::class])
internal class UserListModule {

    @Module
    interface Binders {

        @Binds
        @UserListScope
        fun bindUserDismissRepository(
            impl: UserDeleteRepositoryImpl
        ): UserDeleteRepository

        @Binds
        @UserListScope
        fun bindUserRepository(
            impl: UserRepositoryImpl
        ): UserRepository
    }
}