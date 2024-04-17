package uz.uzkassa.smartpos.trade.companion.presentation.app.di.manager.modules

import dagger.Module
import dagger.Provides
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.trade.companion.manager.coroutine.CoroutineContextManagerProvider
import javax.inject.Singleton

@Module(includes = [ApplicationModuleCoroutine.Providers::class])
object ApplicationModuleCoroutine {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @Singleton
        fun provideCoroutineContextManagerProvider(): CoroutineContextManagerProvider =
            CoroutineContextManagerProvider.instantiate()

        @JvmStatic
        @Provides
        @Singleton
        fun provideCoroutineContextManager(
            coroutineContextManagerProvider: CoroutineContextManagerProvider
        ): CoroutineContextManager =
            coroutineContextManagerProvider.coroutineContextManager
    }
}