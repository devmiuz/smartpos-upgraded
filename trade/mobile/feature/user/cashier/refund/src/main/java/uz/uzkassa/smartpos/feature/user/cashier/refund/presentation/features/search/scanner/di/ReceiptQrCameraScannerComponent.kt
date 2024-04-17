package uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.search.scanner.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.search.di.RefundReceiptSearchComponent
import uz.uzkassa.smartpos.feature.user.cashier.refund.presentation.features.search.scanner.ReceiptQrCameraScannerFragment

@ReceiptQrCameraScannerScope
@Component(dependencies = [RefundReceiptSearchComponent::class])
internal interface ReceiptQrCameraScannerComponent {

    fun inject(fragment: ReceiptQrCameraScannerFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: RefundReceiptSearchComponent
        ): ReceiptQrCameraScannerComponent
    }

    companion object {

        fun create(component: RefundReceiptSearchComponent) =
            DaggerReceiptQrCameraScannerComponent
                .factory()
                .create(component)
    }
}