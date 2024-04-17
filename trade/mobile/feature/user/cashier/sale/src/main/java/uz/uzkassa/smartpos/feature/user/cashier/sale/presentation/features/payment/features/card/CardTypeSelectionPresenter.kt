package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation.features.payment.features.card

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.payment.ReceiptPayment.Type
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sale.payment.card.CardTypeAmountInteractor
import uz.uzkassa.smartpos.feature.user.cashier.sale.domain.sale.payment.card.CardTypeSelectionInteractor
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.properties.Delegates

internal class CardTypeSelectionPresenter @Inject constructor(
    private val cardTypeAmountInteractor: CardTypeAmountInteractor,
    private val cardTypeSelectionInteractor: CardTypeSelectionInteractor
) : MvpPresenter<CardTypeSelectionView>() {
    var amount: BigDecimal by Delegates.notNull()

    override fun onFirstViewAttach() {
        getCardTypes()
    }

    private fun getCardTypes() {
        cardTypeSelectionInteractor
            .getCardType()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCardTypes() }
            .onSuccess { viewState.onSuccessCardTypes(it) }
            .onFailure { viewState.onFailureCardTypes(it) }
    }

    fun setCardType(value: Type) {
        cardTypeAmountInteractor.setCardType(amount, value)
            .launchCatchingIn(presenterScope)
            .onSuccess { if (it) viewState.onSuccessPayment(); dismiss() }
            .onFailure { viewState.onFailurePayment(it) }
    }

    fun dismiss() =
        viewState.onDismissView()
}