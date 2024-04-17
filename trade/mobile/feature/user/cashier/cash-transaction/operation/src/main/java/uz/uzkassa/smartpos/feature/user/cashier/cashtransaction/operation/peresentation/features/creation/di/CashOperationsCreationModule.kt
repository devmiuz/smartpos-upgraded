package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.creation.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.model.operation.CashOperation
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.channel.CashAmountBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.channel.CashOperationBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.amount.CashAmountRepository
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.operation.CashOperationsRepository
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.domain.CashOperationsInteractor
import java.math.BigDecimal

@Module(includes = [CashOperationsCreationModule.Providers::class])
internal object CashOperationsCreationModule {

    @Module
    object Providers {

        @JvmStatic
        @Provides
        @CashOperationsCreationScope
        fun providerCashAmountBroadcastChannel(
        ): CashAmountBroadcastChannel =
            CashAmountBroadcastChannel()

        @FlowPreview
        @JvmStatic
        @Provides
        @CashOperationsCreationScope
        fun providerCashAmountLazyFlow(
            cashAmountBroadcastChannel: CashAmountBroadcastChannel
        ): Flow<BigDecimal> =
            cashAmountBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @CashOperationsCreationScope
        fun providerCashOperationBroadcastChannel(
        ): CashOperationBroadcastChannel =
            CashOperationBroadcastChannel()

        @FlowPreview
        @JvmStatic
        @Provides
        @CashOperationsCreationScope
        fun providerCashOperationLazyFlow(
            cashOperationBroadcastChannel: CashOperationBroadcastChannel
        ): Flow<CashOperation> =
            cashOperationBroadcastChannel.asFlow()

        @JvmStatic
        @Provides
        @CashOperationsCreationScope
        fun provideCashOperationInteractor(
            cashAmountRepository: CashAmountRepository,
            cashOperationBroadcastChannel: CashOperationBroadcastChannel,
            cashOperationsRepository: CashOperationsRepository,
            coroutineContextManager: CoroutineContextManager
        ): CashOperationsInteractor =
            CashOperationsInteractor(
                cashAmountRepository,
                cashOperationBroadcastChannel,
                cashOperationsRepository,
                coroutineContextManager
            )
    }
}