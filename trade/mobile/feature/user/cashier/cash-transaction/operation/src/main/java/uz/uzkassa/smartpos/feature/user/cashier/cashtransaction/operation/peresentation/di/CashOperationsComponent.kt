package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.amount.CashAmountRepository
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.operation.CashOperationsRepository
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.operation.save.CashTransactionSaveRepository
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.dependencies.CashierCashOperationsFeatureDependencies
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.CashOperationsFragment
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.peresentation.navigation.CashOperationsRouter

@CashOperationsScope
@Component(
    dependencies = [CashierCashOperationsFeatureDependencies::class],
    modules = [
        CashOperationsModule::class,
        CashOperationsModuleNavigation::class
    ]
)
abstract class CashOperationsComponent : CashierCashOperationsFeatureDependencies {

    internal abstract val cashAmountRepository: CashAmountRepository

    internal abstract val cashOperationsRepository: CashOperationsRepository

    internal abstract val cashOperationsRouter: CashOperationsRouter

    internal abstract val cashTransactionSaveRepository: CashTransactionSaveRepository

    internal abstract fun inject(fragment: CashOperationsFragment)

    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: CashierCashOperationsFeatureDependencies
        ): CashOperationsComponent
    }

    internal companion object {
        fun create(
            dependencies: CashierCashOperationsFeatureDependencies
        ): CashOperationsComponent =
            DaggerCashOperationsComponent
                .factory()
                .create(dependencies)
    }
}