package uz.uzkassa.common.feature.browser.data.repository

import uz.uzkassa.common.feature.browser.data.model.WebPage
import uz.uzkassa.common.feature.browser.dependencies.BrowserFeatureArgs
import javax.inject.Inject

internal class BrowserRepositoryImpl @Inject constructor(
    private val browserFeatureArgs: BrowserFeatureArgs
) : BrowserRepository {

    override fun getWebPage(): WebPage {
        return with(browserFeatureArgs) { WebPage(contentUrl, title) }
    }
}