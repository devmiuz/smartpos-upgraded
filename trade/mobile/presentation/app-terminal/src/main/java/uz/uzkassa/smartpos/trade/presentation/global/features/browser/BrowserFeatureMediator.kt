package uz.uzkassa.smartpos.trade.presentation.global.features.browser

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen
import uz.uzkassa.common.feature.browser.dependencies.BrowserFeatureArgs
import uz.uzkassa.common.feature.browser.dependencies.BrowserFeatureCallback
import uz.uzkassa.common.feature.browser.presentation.BrowserFragment
import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource
import uz.uzkassa.smartpos.core.utils.resource.string.StringResource
import uz.uzkassa.smartpos.trade.R
import uz.uzkassa.smartpos.trade.presentation.global.features.browser.runner.BrowserFeatureRunner
import uz.uzkassa.smartpos.trade.presentation.support.feature.FeatureMediator
import kotlin.properties.Delegates

class BrowserFeatureMediator(
    private val router: Router
) : FeatureMediator, BrowserFeatureArgs, BrowserFeatureCallback {
    override var contentUrl: String by Delegates.notNull()
    override val title = object : LocalizableResource {
        override val resourceString =
            StringResource(R.string.browser_feature_terms_of_use_title)
    }

    val featureRunner: BrowserFeatureRunner =
        FeatureRunnerImpl()

    private inner class FeatureRunnerImpl : BrowserFeatureRunner {
        override fun run(contentUrl: String, action: (Screen) -> Unit) {
            this@BrowserFeatureMediator.contentUrl = contentUrl
            router.navigateTo(Screens.Browser)
        }
    }

    private object Screens {
        object Browser : SupportAppScreen() {
            override fun getFragment(): Fragment =
                BrowserFragment.newInstance()
        }
    }
}