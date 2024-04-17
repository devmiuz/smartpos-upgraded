package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.payment.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.cashier.refund.data.repository.receipt.save.ReceiptSaveRepository
import uz.uzkassa.smartpos.feature.user.cashier.refund.dependencies.CashierRefundFeatureDependencies
import uz.uzkassa.smartpos.feature.user.cashier.refund.domain.RefundInteractor
import uz.uzkassa.smartpos.feature.user.cashier.refund.domain.product.marking.ProductMarkingInteractor
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.di.CashierRefundComponent
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.payment.RefundPaymentFragment
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.navigation.RefundRouter

@RefundPaymentScope
@Component(dependencies = [CashierRefundComponent::class], modules = [RefundPaymentModule::class])
internal interface RefundPaymentComponent : CashierRefundFeatureDependencies {

    fun inject(fragment: RefundPaymentFragment)

    val receiptSaveRepository: ReceiptSaveRepository

    val refundInteractor: RefundInteractor

    val productMarkingInteractor : ProductMarkingInteractor

    val refundRouter: RefundRouter

    @Component.Factory
    interface Factory {

        fun create(
            component: CashierRefundComponent
        ): RefundPaymentComponent
    }

    companion object {

        fun create(component: CashierRefundComponent): RefundPaymentComponent =
            DaggerRefundPaymentComponent
                .factory()
                .create(component)
    }
}