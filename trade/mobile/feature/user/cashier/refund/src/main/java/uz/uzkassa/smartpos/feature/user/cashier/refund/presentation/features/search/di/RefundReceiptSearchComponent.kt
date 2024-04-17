package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.search.di

import dagger.Component
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.receipt.ReceiptRepository
import uz.uzkassa.smartpos.feature.user.cashier.refund.domain.RefundInteractor
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.di.CashierRefundComponent
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.search.RefundReceiptSearchFragment
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.navigation.RefundRouter

@RefundReceiptSearchScope
@Component(
    dependencies = [CashierRefundComponent::class],
    modules = [RefundReceiptSearchModule::class]
)
internal interface RefundReceiptSearchComponent {

    val coroutineContextManager: CoroutineContextManager

    val receiptRepository: ReceiptRepository

    val refundInteractor: RefundInteractor

    val refundRouter: RefundRouter

    fun inject(fragment: RefundReceiptSearchFragment)

    @Component.Factory
    interface Factory {

        fun create(
            component: CashierRefundComponent
        ): RefundReceiptSearchComponent
    }

    companion object {

        fun create(component: CashierRefundComponent): RefundReceiptSearchComponent =
            DaggerRefundReceiptSearchComponent
                .factory()
                .create(component)
    }
}