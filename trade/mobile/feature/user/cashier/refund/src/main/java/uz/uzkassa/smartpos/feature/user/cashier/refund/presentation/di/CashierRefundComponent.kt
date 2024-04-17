package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.receipt.save.ReceiptSaveRepository
import uz.uzkassa.smartpos.feature.user.cashier.refund.dependencies.CashierRefundFeatureDependencies
import uz.uzkassa.smartpos.feature.user.cashier.refund.domain.RefundInteractor
import uz.uzkassa.smartpos.feature.user.cashier.refund.domain.product.marking.ProductMarkingInteractor
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.CashierRefundFragment
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.navigation.RefundRouter

@CashierRefundScope
@Component(
    dependencies = [CashierRefundFeatureDependencies::class],
    modules = [CashierRefundModule::class, CashierRefundModuleNavigation::class]
)
abstract class CashierRefundComponent : CashierRefundFeatureDependencies {

    internal abstract val receiptSaveRepository: ReceiptSaveRepository

    internal abstract val refundInteractor: RefundInteractor

    internal abstract val productMarkingInteractor : ProductMarkingInteractor

    internal abstract val refundRouter: RefundRouter

    internal abstract fun inject(fragment: CashierRefundFragment)

    @Component.Factory
    internal interface Factory {

        fun create(
            dependencies: CashierRefundFeatureDependencies
        ): CashierRefundComponent
    }

    internal companion object {

        fun create(dependencies: CashierRefundFeatureDependencies): CashierRefundComponent =
            DaggerCashierRefundComponent
                .factory()
                .create(dependencies)
    }
}