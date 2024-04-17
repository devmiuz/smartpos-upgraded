package uz.uzkassa.smartpos.feature.company.vat.selection.presentation

import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVAT
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.company.vat.selection.dependencies.CompanyVATSelectionFeatureCallback
import uz.uzkassa.smartpos.feature.company.vat.selection.domain.CompanyVATSelectionInteractor
import javax.inject.Inject

internal class CompanyVATSelectionPresenter @Inject constructor(
    private val companyVATSelectionFeatureCallback: CompanyVATSelectionFeatureCallback,
    private val companyVATSelectionInteractor: CompanyVATSelectionInteractor
) : MvpPresenter<CompanyVATSelectionView>() {

    override fun onFirstViewAttach() =
        getCompanyVAT()

    fun getCompanyVAT() {
        companyVATSelectionInteractor
            .getCompanyVAT()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCompanyVAT() }
            .onSuccess { viewState.onSuccessCompanyVAT(it) }
            .onFailure { viewState.onErrorCompanyVAT(it) }
    }

    fun setCompanyVAT(companyVAT: CompanyVAT) =
        companyVATSelectionInteractor.setCompanyVAT(companyVAT)

    fun selectCompanyVAT() {
        companyVATSelectionInteractor.getSelectedCompanyVAT()?.let {
            companyVATSelectionFeatureCallback.onFinishCompanyVATSelection(it)
            viewState.onDismissView()
        }
    }

    fun dismiss() =
        viewState.onDismissView()
}