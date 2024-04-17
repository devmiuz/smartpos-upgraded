package uz.uzkassa.common.feature.browser.presentation

import moxy.MvpView
import uz.uzkassa.common.feature.browser.data.model.WebPage

internal interface BrowserView : MvpView {

    fun onContentUrlDefined(webPage: WebPage)

    fun onLoadingWebPage()

    fun onSuccessWebPage()

    fun onErrorWebPage(throwable: Throwable)

    fun onDismissView()
}