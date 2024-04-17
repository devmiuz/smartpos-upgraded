package uz.uzkassa.common.feature.browser.data.repository

import uz.uzkassa.common.feature.browser.data.model.WebPage

internal interface BrowserRepository {

    fun getWebPage(): WebPage
}