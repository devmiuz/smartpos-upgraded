package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.credit_advance.list.scanner.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.credit_advance.list.di.CreditAdvanceListComponent
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.credit_advance.list.scanner.ReceiptQrCameraScannerFragment

@ReceiptQrCameraScannerScope
@Component(dependencies = [CreditAdvanceListComponent::class])
internal interface ReceiptQrCameraScannerComponent {

    fun inject(fragment: ReceiptQrCameraScannerFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: CreditAdvanceListComponent
        ): ReceiptQrCameraScannerComponent
    }

    companion object {

        fun create(component: CreditAdvanceListComponent) =
            DaggerReceiptQrCameraScannerComponent
                .factory()
                .create(component)
    }
}