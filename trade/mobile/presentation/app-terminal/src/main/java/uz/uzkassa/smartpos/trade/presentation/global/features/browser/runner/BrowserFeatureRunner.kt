package uz.uzkassa.smartpos.trade.presentation.global.features.browser.runner

import ru.terrakok.cicerone.Screen

interface BrowserFeatureRunner {

    fun run(contentUrl: String, action: (Screen) -> Unit)
}