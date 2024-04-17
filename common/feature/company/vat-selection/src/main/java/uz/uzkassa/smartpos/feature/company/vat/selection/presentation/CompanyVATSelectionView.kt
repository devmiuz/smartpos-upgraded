package uz.uzkassa.smartpos.feature.company.vat.selection.presentation

import moxy.MvpView
import uz.uzkassa.smartpos.feature.company.vat.selection.data.model.CompanyVATSelection

internal interface CompanyVATSelectionView : MvpView {

    fun onLoadingCompanyVAT()

    fun onSuccessCompanyVAT(companyVAT: List<CompanyVATSelection>)

    fun onErrorCompanyVAT(throwable: Throwable)

    fun onDismissView()
}