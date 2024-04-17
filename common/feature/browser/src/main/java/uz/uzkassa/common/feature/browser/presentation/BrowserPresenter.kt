package uz.uzkassa.common.feature.browser.presentation

import moxy.MvpPresenter
import uz.uzkassa.common.feature.browser.domain.BrowserInteractor
import javax.inject.Inject

internal class BrowserPresenter @Inject constructor(
    private val browserInteractor: BrowserInteractor
) : MvpPresenter<BrowserView>() {

    override fun onFirstViewAttach() {
        viewState.onContentUrlDefined(browserInteractor.getWebPageContent())
    }

    fun setLoadingWebPage() =
        viewState.onLoadingWebPage()

    fun setSuccessWebPage() =
        viewState.onSuccessWebPage()

    fun dismiss() =
        viewState.onDismissView()
}