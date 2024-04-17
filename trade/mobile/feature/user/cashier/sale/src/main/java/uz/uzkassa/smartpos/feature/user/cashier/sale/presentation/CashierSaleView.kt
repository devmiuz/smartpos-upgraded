package uz.uzkassa.smartpos.feature.user.cashier.sale.presentation

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.language.model.Language

internal interface CashierSaleView : MvpView {

    fun onLoadingUserLanguage()

    fun onSuccessUserLanguage(language: Language)
}