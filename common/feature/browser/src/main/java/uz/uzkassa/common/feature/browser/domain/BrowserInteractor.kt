package uz.uzkassa.common.feature.browser.domain

import uz.uzkassa.common.feature.browser.data.model.WebPage
import uz.uzkassa.common.feature.browser.data.repository.BrowserRepository
import javax.inject.Inject

internal class BrowserInteractor @Inject constructor(
    private val browserRepository: BrowserRepository
) {

    fun getWebPageContent(): WebPage =
        browserRepository.getWebPage()

}