package uz.uzkassa.apay.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import uz.uzkassa.apay.data.channel.BillIdBroadcastChannel
import uz.uzkassa.apay.data.channel.ClientIdBroadcastChannel
import uz.uzkassa.apay.data.channel.SocketBroadcastChannel
import uz.uzkassa.apay.data.channel.StompBroadcastChannel
import uz.uzkassa.apay.data.model.SocketData
import uz.uzkassa.apay.data.repository.ApayPaymentRepository
import uz.uzkassa.apay.data.repository.ApayPaymentRepositoryImpl
import uz.uzkassa.apay.dependencies.CashierApayFeatureArgs
import uz.uzkassa.apay.dependencies.CashierApayFeatureCallback
import uz.uzkassa.apay.domain.ApayInteractor
import uz.uzkassa.apay.presentation.features.qr.generator.di.QrGeneratorScope
import uz.uzkassa.apay.presentation.navigation.CashierApayRouter
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.launcher.data.preference.branch.CurrentBranchPreference

@Module(
    includes = [
        CashierApayModule.Providers::class,
        CashierApayModule.Binders::class
    ]
)
internal object CashierApayModule {

    @Module
    interface Binders {

        @Binds
        @CashierApayScope
        fun bindApayPaymentRepository(
            impl: ApayPaymentRepositoryImpl
        ): ApayPaymentRepository
    }

    @Module
    object Providers {
        private val cicerone: Cicerone<Router> = Cicerone.create()

        @JvmStatic
        @Provides
        @CashierApayScope
        fun provideSaleRouter(
            cashierApayFeatureArgs: CashierApayFeatureArgs,
            cashierApayFeatureCallback: CashierApayFeatureCallback
        ): CashierApayRouter =
            CashierApayRouter(
                cashierApayFeatureArgs,
                cashierApayFeatureCallback,
                router = cicerone.router
            )

        @JvmStatic
        @Provides
        fun provideNavigatorHolder(): NavigatorHolder =
            cicerone.navigatorHolder


        @JvmStatic
        @Provides
        @CashierApayScope
        fun provideApayInteractor(
            coroutineContextManager: CoroutineContextManager,
            apayPaymentRepository: ApayPaymentRepository,
            currentBranchPreference: CurrentBranchPreference,
            args: CashierApayFeatureArgs
        ): ApayInteractor =
            ApayInteractor(
                coroutineContextManager,
                apayPaymentRepository,
                currentBranchPreference,
                args
            )

        @JvmStatic
        @Provides
        @CashierApayScope
        fun provideStompClient(): StompBroadcastChannel =
            StompBroadcastChannel()

        @JvmStatic
        @Provides
        @CashierApayScope
        fun provideBillIdClient(): BillIdBroadcastChannel =
            BillIdBroadcastChannel()

        @JvmStatic
        @Provides
        @CashierApayScope
        fun provideClientIdClient(): ClientIdBroadcastChannel =
            ClientIdBroadcastChannel()

        @JvmStatic
        @Provides
        @CashierApayScope
        fun provideSocketClient(): SocketBroadcastChannel =
            SocketBroadcastChannel()
    }
}