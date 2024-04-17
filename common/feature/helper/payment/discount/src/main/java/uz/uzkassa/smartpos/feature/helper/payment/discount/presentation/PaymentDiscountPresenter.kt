package uz.uzkassa.smartpos.feature.helper.payment.discount.presentation

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.Discount
import uz.uzkassa.smartpos.feature.helper.payment.discount.data.model.DiscountArbitrary
import uz.uzkassa.smartpos.feature.helper.payment.discount.dependencies.PaymentDiscountFeatureCallback
import uz.uzkassa.smartpos.feature.helper.payment.discount.presentation.navigation.DiscountRouter
import javax.inject.Inject

internal class PaymentDiscountPresenter @Inject constructor(
    private val discountArbitraryLazyFlow: Lazy<Flow<DiscountArbitrary>>,
    private val discountRouter: DiscountRouter,
    private val paymentDiscountFeatureCallback: PaymentDiscountFeatureCallback
) : MvpPresenter<PaymentDiscountView>() {

    override fun onFirstViewAttach() {
        discountRouter.openDiscountArbitraryScreen()
        getProvidedDiscountArbitrary()
    }

    private fun getProvidedDiscountArbitrary() {
        discountArbitraryLazyFlow.get()
            .onEach {
                paymentDiscountFeatureCallback.onFinish(Discount(it))
                discountRouter.exit()
            }
            .launchIn(presenterScope)
    }
}