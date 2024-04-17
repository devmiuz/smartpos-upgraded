package uz.uzkassa.smartpos.feature.company.saving.presentation.creation

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessType
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVAT
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.company.saving.data.exception.CompanyAlreadyCreatedException
import uz.uzkassa.smartpos.feature.company.saving.data.exception.CompanyCreationException
import uz.uzkassa.smartpos.feature.company.saving.data.model.RegionCitySelectionType
import uz.uzkassa.smartpos.feature.company.saving.dependencies.CompanySavingFeatureCallback
import uz.uzkassa.smartpos.feature.company.saving.domain.business.CompanyBusinessTypeInteractor
import uz.uzkassa.smartpos.feature.company.saving.domain.creation.CompanyCreationInteractor
import javax.inject.Inject

internal class CompanyCreationPresenter @Inject constructor(
    private val companyBusinessTypeInteractor: CompanyBusinessTypeInteractor,
    private val companyCreationInteractor: CompanyCreationInteractor,
    private val activityTypesLazyFlow: Lazy<Flow<List<ActivityType>>>,
    private val companySavingFeatureCallback: CompanySavingFeatureCallback,
    private val companyVATLazyFlow: Lazy<Flow<CompanyVAT>>,
    private val regionCityPairLazyFlow: Lazy<Flow<Pair<Region, City>>>
) : MvpPresenter<CompanyCreationView>() {

    override fun onFirstViewAttach() {
        getCompanyBusinessTypes()
        getActivityTypes()
        getCompanyVAT()
        getRegionCityPair()
    }

    fun getCompanyBusinessTypes() {
        companyBusinessTypeInteractor
            .getCompanyBusinessTypes()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCompanyBusinessTypes() }
            .onSuccess { viewState.onSuccessCompanyBusinessTypes(it) }
            .onFailure { viewState.onErrorCompanyBusinessTypes(it) }
    }

    fun setOwnerFirstName(value: String) =
        companyCreationInteractor.setOwnerFirstName(value)

    fun setOwnerLastName(value: String) =
        companyCreationInteractor.setOwnerLastName(value)

    fun removeActivityType(activityType: ActivityType) {
        companyCreationInteractor.removeActivityType(activityType)
        viewState.onActivityTypeRemoved(activityType)
    }

    fun setCompanyBusinessType(entity: CompanyBusinessType) {
        companyCreationInteractor.setCompanyBusinessType(entity)
        viewState.onCompanyBusinessTypeSelected(entity)
    }

    fun setCompanyName(value: String) =
        companyCreationInteractor.setName(value)

    fun setCompanyAddress(value: String) =
        companyCreationInteractor.setAddress(value)

    fun openActivityTypeSelectionScreen() {
        companyCreationInteractor.getActivityTypes().let {
            companySavingFeatureCallback.onOpenActivityTypeSelection(it)
        }
    }

    fun openRegionSelectionScreen() {
        companyCreationInteractor.getRegionCityPair().let {
            companySavingFeatureCallback
                .onOpenRegionCitySelection(it.first, it.second, RegionCitySelectionType.REGION)
        }
    }

    fun openCitySelectionScreen() {
        companyCreationInteractor.getRegionCityPair().let {
            companySavingFeatureCallback
                .onOpenRegionCitySelection(it.first, it.second, RegionCitySelectionType.CITY)
        }
    }

    fun toggleCompanyVAT(isEnabled: Boolean) {
        if (!isEnabled) openCompanyVATSelectionView()
        else {
            companyCreationInteractor.setCompanyVAT(null)
            viewState.onCompanyVATChanged(null, false)
        }
    }

    fun openCompanyVATSelectionView() {
        companyCreationInteractor.getCompanyVAT().let {
            companySavingFeatureCallback.onOpenCompanyVATSelection(it)
        }
    }

    fun createCompany() {
        companyCreationInteractor
            .createOwner()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCreation() }
            .onSuccess { companySavingFeatureCallback.onFinishCompanySaving() }
            .onFailure {
                when (it) {
                    is CompanyCreationException -> {
                        if (it.isActivityTypesNotDefined)
                            viewState.onErrorCreation(it)

                        if (it.isOwnerLastNameNotDefined)
                            viewState.onErrorCreationCauseOwnerLastNameNotDefined()

                        if (it.isOwnerFirstNameNotDefined)
                            viewState.onErrorCreationCauseOwnerFirstNameNotDefined()

                        if (it.isCompanyBusinessTypeNotDefined)
                            viewState.onErrorCreationCauseCompanyBusinessTypeNotDefined()

                        if (it.isNameNotDefined)
                            viewState.onErrorCreationCauseNameNotDefined()

                        if (it.isAddressNotDefined)
                            viewState.onErrorCreationCauseAddressNotDefined()

                        if (it.isRegionNotDefined)
                            viewState.onErrorCreationCauseRegionNotDefined()

                        if (it.isCityNotDefined)
                            viewState.onErrorCreationCauseCityNotDefined()
                    }

                    is CompanyAlreadyCreatedException ->
                        companySavingFeatureCallback.onFinishCompanySaving()

                    else -> viewState.onErrorCreation(it)
                }
            }
    }

    private fun getActivityTypes() {
        activityTypesLazyFlow.get()
            .onEach {
                companyCreationInteractor.setActivityTypes(it)
                viewState.onActivityTypesChanged(it)
            }
            .launchIn(presenterScope)
    }

    private fun getRegionCityPair() {
        regionCityPairLazyFlow.get()
            .onEach {
                companyCreationInteractor.setRegion(it.first)
                companyCreationInteractor.setCity(it.second)
                viewState.onRegionCityChanged(it.first, it.second)
            }
            .launchIn(presenterScope)
    }

    private fun getCompanyVAT() {
        companyVATLazyFlow.get()
            .onEach {
                companyCreationInteractor.setCompanyVAT(it)
                viewState.onCompanyVATChanged(it, true)
            }
            .launchIn(presenterScope)
    }
}