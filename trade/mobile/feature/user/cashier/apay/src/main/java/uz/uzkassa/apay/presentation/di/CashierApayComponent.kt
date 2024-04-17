package uz.uzkassa.apay.presentation.di

import dagger.Component
import uz.uzkassa.apay.data.channel.BillIdBroadcastChannel
import uz.uzkassa.apay.data.channel.ClientIdBroadcastChannel
import uz.uzkassa.apay.data.channel.SocketBroadcastChannel
import uz.uzkassa.apay.data.channel.StompBroadcastChannel
import uz.uzkassa.apay.dependencies.CashierApayFeatureDependencies
import uz.uzkassa.apay.domain.ApayInteractor
import uz.uzkassa.apay.presentation.CashierApayFragment
import uz.uzkassa.apay.presentation.navigation.CashierApayRouter

@CashierApayScope
@Component(
    dependencies = [CashierApayFeatureDependencies::class],
    modules = [CashierApayModule::class]
)
abstract class CashierApayComponent : CashierApayFeatureDependencies {

    abstract fun inject(fragment: CashierApayFragment)

    internal abstract val cashierApayRouter: CashierApayRouter

    internal abstract val apayInteractor: ApayInteractor

    internal abstract val stompBroadcastChannel: StompBroadcastChannel

    internal abstract val billIdBroadcastChannel: BillIdBroadcastChannel

    internal abstract val clientIdBroadcastChannel: ClientIdBroadcastChannel

    internal abstract val socketBroadcastChannel: SocketBroadcastChannel


    @Component.Factory
    interface Factory {
        fun create(
            dependenciesCashier: CashierApayFeatureDependencies
        ): CashierApayComponent
    }

    companion object {

        fun create(dependenciesCashier: CashierApayFeatureDependencies): CashierApayComponent =
            DaggerCashierApayComponent
                .factory()
                .create(dependenciesCashier)
    }
}