package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.di

import dagger.Component
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.drawerlayout.DrawerStateDelegate
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.receipt.draft.ReceiptDraftProductsBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.channel.receipt.held.ReceiptHeldBroadcastChannel
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.receipt.save.ReceiptSaleSaveRepository
import uz.uzkassa.smartpos.feature.user.cashier.sale.dependencies.CashierSaleFeatureDependencies
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.SaleInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.product.marking.ProductMarkingInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.CashierSaleFragment
import uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.navigation.CashierSaleRouter

@CashierSaleScope
@Component(
    dependencies = [CashierSaleFeatureDependencies::class],
    modules = [CashierSaleModule::class, CashierSaleModuleNavigation::class]
)
abstract class CashierSaleComponent : CashierSaleFeatureDependencies {

    internal abstract fun inject(fragment: CashierSaleFragment)

    internal abstract val cashierSaleRouter: CashierSaleRouter

    internal abstract val saleInteractor: SaleInteractor

    internal abstract val drawerStateDelegate: DrawerStateDelegate

    internal abstract val receiptDraftProductsBroadcastChannel: ReceiptDraftProductsBroadcastChannel

    internal abstract val receiptHeldBroadcastChannel: ReceiptHeldBroadcastChannel

    internal abstract val receiptSaleSaveRepository: ReceiptSaleSaveRepository

    internal abstract val productMarkingInteractor : ProductMarkingInteractor



    @Component.Factory
    internal interface Factory {
        fun create(
            dependencies: CashierSaleFeatureDependencies
        ): CashierSaleComponent
    }

    internal companion object {

        fun create(dependencies: CashierSaleFeatureDependencies): CashierSaleComponent =
            DaggerCashierSaleComponent
                .factory()
                .create(dependencies)
    }
}