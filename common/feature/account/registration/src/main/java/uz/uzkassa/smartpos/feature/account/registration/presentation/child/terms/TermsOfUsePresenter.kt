package uz.uzkassa.smartpos.feature.account.registration.presentation.child.terms

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.feature.account.registration.dependencies.AccountRegistrationFeatureCallback
import uz.uzkassa.smartpos.feature.account.registration.domain.RegistrationInteractor
import uz.uzkassa.smartpos.feature.account.registration.presentation.navigation.RegistrationRouter
import javax.inject.Inject

internal class TermsOfUsePresenter @Inject constructor(
    private val accountRegistrationFeatureCallback: AccountRegistrationFeatureCallback,
    private val registrationInteractor: RegistrationInteractor,
    private val registrationRouter: RegistrationRouter
) : MvpPresenter<TermsOfUseView>() {
    private var isPhoneNumberDefined: Boolean = false
    private var isTermsOfUseChecked: Boolean = false

    override fun onFirstViewAttach() =
        viewState.onDataChanging(isPhoneNumberDefined, isTermsOfUseChecked)

    fun setPhoneNumber(phoneNumber: String) {
        registrationInteractor.setPhoneNumber(phoneNumber)
            .onEach {
                isPhoneNumberDefined = it
                viewState.onDataChanging(it, isTermsOfUseChecked)
            }
            .launchIn(presenterScope)
    }

    fun checkTermsOfUse(isChecked: Boolean) {
        isTermsOfUseChecked = isChecked
        registrationInteractor.acceptTermsOfUse(isChecked)
        viewState.onDataChanging(isPhoneNumberDefined, isTermsOfUseChecked)
    }

    fun startProcessContinue() =
        viewState.onShowConfirmationAlert()

    fun showTermsOfUseScreen() {
        val offerUrl: String = "https://drive.google.com/viewerng/viewer?embedded=true&url=" +
                "https://storage.smartpos.uz/oferta_smartpos.pdf"
        accountRegistrationFeatureCallback.onOpenTermsOfUsage(offerUrl)
    }

    fun proceed() {
        viewState.onDismissConfirmationAlert()
        registrationRouter.openConfirmationCode()
    }

    fun cancelProcessContinue() =
        viewState.onDismissConfirmationAlert()
}