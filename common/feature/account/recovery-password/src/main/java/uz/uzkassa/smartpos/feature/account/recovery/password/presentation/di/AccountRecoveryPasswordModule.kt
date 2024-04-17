package uz.uzkassa.smartpos.feature.account.recovery.password.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.account.recovery.password.data.repository.RecoveryPasswordRepository
import uz.uzkassa.smartpos.feature.account.recovery.password.data.repository.RecoveryPasswordRepositoryImpl
import uz.uzkassa.smartpos.feature.account.recovery.password.domain.RecoveryPasswordInteractor

@Module(
    includes = [
        AccountRecoveryPasswordModule.Binders::class,
        AccountRecoveryPasswordModule.Providers::class
    ]
)
internal object AccountRecoveryPasswordModule {

    @Module
    interface Binders {

        @Binds
        @AccountRecoveryPasswordScope
        fun bindPasswordRecoveryRepository(
            impl: RecoveryPasswordRepositoryImpl
        ): RecoveryPasswordRepository
    }

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @AccountRecoveryPasswordScope
        fun providePasswordRecoveryInteractor(
            coroutineContextManager: CoroutineContextManager,
            recoveryPasswordRepository: RecoveryPasswordRepository
        ): RecoveryPasswordInteractor =
            RecoveryPasswordInteractor(
                coroutineContextManager = coroutineContextManager,
                recoveryPasswordRepository = recoveryPasswordRepository
            )
    }
}