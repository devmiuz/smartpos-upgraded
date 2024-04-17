package uz.uzkassa.smartpos.feature.account.registration.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.account.registration.data.repository.RegistrationRepository
import uz.uzkassa.smartpos.feature.account.registration.data.repository.RegistrationRepositoryImpl
import uz.uzkassa.smartpos.feature.account.registration.domain.RegistrationInteractor

@Module(
    includes = [
        AccountRegistrationModule.Binders::class,
        AccountRegistrationModule.Providers::class
    ]
)
internal object AccountRegistrationModule {

    @Module
    interface Binders {

        @Binds
        @AccountRegistrationScope
        fun bindRegistrationRepository(
            impl: RegistrationRepositoryImpl
        ): RegistrationRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @AccountRegistrationScope
        fun provideRegistrationInteractor(
            coroutineContextManager: CoroutineContextManager,
            registrationRepository: RegistrationRepository
        ): RegistrationInteractor =
            RegistrationInteractor(
                registrationRepository = registrationRepository,
                coroutineContextManager = coroutineContextManager
            )
    }
}