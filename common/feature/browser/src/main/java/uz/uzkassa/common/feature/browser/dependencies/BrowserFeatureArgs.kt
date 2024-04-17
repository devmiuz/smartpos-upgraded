package uz.uzkassa.common.feature.browser.dependencies

import uz.uzkassa.smartpos.core.utils.resource.LocalizableResource

interface BrowserFeatureArgs {

    val contentUrl: String

    val title: LocalizableResource
}