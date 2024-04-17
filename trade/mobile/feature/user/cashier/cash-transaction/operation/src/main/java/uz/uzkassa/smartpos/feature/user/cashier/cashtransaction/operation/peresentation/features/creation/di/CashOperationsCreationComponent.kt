package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.creation.di

import dagger.Component
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.amount.CashAmountRepository
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.operation.CashOperationsRepository
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.operation.save.CashTransactionSaveRepository
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.domain.CashOperationsInteractor
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.di.CashOperationsComponent
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.features.creation.CashOperationsCreationFragment
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.navigation.CashOperationsRouter

@CashOperationsCreationScope
@Component(
    dependencies = [CashOperationsComponent::class],
    modules = [CashOperationsCreationModule::class]
)
internal interface CashOperationsCreationComponent {

    fun inject(fragment: CashOperationsCreationFragment)

    val cashAmountRepository: CashAmountRepository

    val cashOperationsInteractor: CashOperationsInteractor

    val cashOperationsRouter: CashOperationsRouter

    val cashOperationsRepository: CashOperationsRepository

    val cashTransactionSaveRepository: CashTransactionSaveRepository

    val coroutineContextManager: CoroutineContextManager

    @Component.Factory
    interface Factory {
        fun create(
            component: CashOperationsComponent
        ): CashOperationsCreationComponent
    }

    companion object {
        fun create(
            component: CashOperationsComponent
        ): CashOperationsCreationComponent =
            DaggerCashOperationsCreationComponent
                .factory()
                .create(component)
    }
}