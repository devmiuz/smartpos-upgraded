package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.di

import dagger.Component
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.receipt.held.ReceiptHeldBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.sale.SalePaymentBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.save.ReceiptSaleSaveRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureDependencies
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.SaleInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.product.marking.ProductMarkingInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sale.payment.SalePaymentInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.di.CashierSaleComponent
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.SalePaymentFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.navigation.CashierSaleRouter

@SalePaymentScope
@Component(
    dependencies = [CashierSaleComponent::class],
    modules = [SalePaymentModule::class]
)
internal interface SalePaymentComponent : CashierSaleFeatureDependencies {

//    val deviceInfoManager: DeviceInfoManager
//
//    val coroutineContextManager: CoroutineContextManager
//
//    val gtposPaymentSource: GTPOSPaymentSource

    val receiptHeldBroadcastChannel: ReceiptHeldBroadcastChannel

    val receiptSaleSaveRepository: ReceiptSaleSaveRepository

    val saleInteractor: SaleInteractor

    val salePaymentInteractor: SalePaymentInteractor

    val cashierSaleRouter: CashierSaleRouter

    val productMarkingInteractor: ProductMarkingInteractor

    val salePaymentBroadcastChannel: SalePaymentBroadcastChannel

    fun inject(fragment: SalePaymentFragment)

    @Component.Factory
    interface Factory {
        fun create(
            component: CashierSaleComponent
        ): SalePaymentComponent
    }

    companion object {

        fun create(component: CashierSaleComponent): SalePaymentComponent =
            DaggerSalePaymentComponent
                .factory()
                .create(component)
    }
}