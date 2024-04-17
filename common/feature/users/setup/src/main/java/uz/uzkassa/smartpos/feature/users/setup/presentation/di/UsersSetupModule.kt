package uz.uzkassa.smartpos.feature.users.setup.presentation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.users.setup.data.UsersRepository
import uz.uzkassa.smartpos.feature.users.setup.data.UsersRepositoryImpl

@Module(includes = [UsersSetupModule.Binders::class])
internal class UsersSetupModule {

    @Module
    interface Binders {

        @Binds
        @UsersSetupScope
        fun bindUserRepository(
            impl: UsersRepositoryImpl
        ): UsersRepository
    }
}