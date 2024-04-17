package uz.uzkassa.smartpos.feature.account.auth.presentation.di

import dagger.Binds
import dagger.Module
import uz.uzkassa.smartpos.feature.account.auth.data.repository.AuthRepository
import uz.uzkassa.smartpos.feature.account.auth.data.repository.AuthRepositoryImpl

@Module(includes = [AccountAuthModule.Binders::class])
internal object AccountAuthModule {

    @Module
    interface Binders {

        @Binds
        @AccountAuthScope
        fun bindAuthRepository(
            impl: AuthRepositoryImpl
        ): AuthRepository
    }
}