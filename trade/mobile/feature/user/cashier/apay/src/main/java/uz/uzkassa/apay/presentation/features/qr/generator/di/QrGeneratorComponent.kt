package uz.uzkassa.apay.presentation.features.qr.generator.di

import dagger.Component
import uz.uzkassa.apay.data.channel.BillIdBroadcastChannel
import uz.uzkassa.apay.data.channel.ClientIdBroadcastChannel
import uz.uzkassa.apay.data.channel.SocketBroadcastChannel
import uz.uzkassa.apay.data.channel.StompBroadcastChannel
import uz.uzkassa.apay.domain.ApayInteractor
import uz.uzkassa.apay.presentation.di.CashierApayComponent
import uz.uzkassa.apay.presentation.features.qr.generator.QrGeneratorFragment
import uz.uzkassa.apay.presentation.navigation.CashierApayRouter

@QrGeneratorScope
@Component(
    dependencies = [CashierApayComponent::class],
    modules = [QrGenaratorApayModule::class]
)
internal interface QrGeneratorComponent {

    fun inject(fragment: QrGeneratorFragment)

    val cashierApayRouter: CashierApayRouter

    @Component.Factory
    interface Factory {

        fun create(
            component: CashierApayComponent
        ): QrGeneratorComponent
    }

    companion object {

        fun create(component: CashierApayComponent): QrGeneratorComponent =
            DaggerQrGeneratorComponent
                .factory()
                .create(component)
    }

}