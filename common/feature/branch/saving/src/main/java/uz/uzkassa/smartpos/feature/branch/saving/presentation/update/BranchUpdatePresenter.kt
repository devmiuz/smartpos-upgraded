package uz.uzkassa.smartpos.feature.branch.saving.presentation.update

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.MvpPresenter
import moxy.presenterScope
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.utils.coroutines.flow.launchCatchingIn
import uz.uzkassa.smartpos.feature.branch.saving.data.exception.BranchSavingException
import uz.uzkassa.smartpos.feature.branch.saving.data.model.RegionCitySelectionType
import uz.uzkassa.smartpos.feature.branch.saving.dependencies.BranchSavingFeatureCallback
import uz.uzkassa.smartpos.feature.branch.saving.domain.update.BranchUpdateInteractor
import javax.inject.Inject

internal class BranchUpdatePresenter @Inject constructor(
    private val activityTypeLazyFlow: Lazy<Flow<ActivityType>>,
    private val branchSavingFeatureCallback: BranchSavingFeatureCallback,
    private val branchUpdateInteractor: BranchUpdateInteractor,
    private val regionCityPairLazyFlow: Lazy<Flow<Pair<Region, City>>>
) : MvpPresenter<BranchUpdateView>() {

    override fun onFirstViewAttach() {
        getActivityType()
        getProvidedRegionCityPair()
        getBranch()
    }

    fun getBranch() {
        branchUpdateInteractor
            .getBranch()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingBranch() }
            .onSuccess { it ->
                viewState.onSuccessBranch(it)
                it.activityType?.let { viewState.onActivityTypeAdded(it) }
                it.region?.let { region ->
                    it.city?.let { viewState.onRegionCityChanged(region, it) }
                }
            }
            .onFailure { viewState.onErrorBranch(it) }
    }

    fun removeActivityType(activityType: ActivityType) {
        branchUpdateInteractor.removeActivityType()
        viewState.onActivityTypeRemoved(activityType)
    }

    fun setName(value: String) =
        branchUpdateInteractor.setName(value)

    fun setAddress(value: String) =
        branchUpdateInteractor.setAddress(value)

    fun updateBranch() {
        branchUpdateInteractor
            .updateBranch()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingUpdate() }
            .onSuccess { backToRootScreen() }
            .onFailure {
                if (it is BranchSavingException) {

                    if (it.isActivityTypeNotDefined)
                        viewState.onErrorUpdate(it)

                    if (it.isNameNotDefined)
                        viewState.onErrorUpdateCauseNameNotDefined()

                    if (it.isRegionNotDefined)
                        viewState.onErrorUpdateCauseRegionNotDefined()

                    if (it.isCityNotDefined)
                        viewState.onErrorUpdateCauseCityNotDefined()

                    if (it.isAddressNotDefined)
                        viewState.onErrorUpdateCauseAddressNotDefined()

                } else viewState.onErrorUpdate(it)
            }
    }

    private fun getActivityType() {
        activityTypeLazyFlow.get()
            .onEach {
                branchUpdateInteractor.setActivityType(it)
                viewState.onActivityTypeAdded(it)
            }
            .launchIn(presenterScope)
    }

    private fun getProvidedRegionCityPair() {
        regionCityPairLazyFlow.get()
            .onEach {
                branchUpdateInteractor.setRegion(it.first)
                branchUpdateInteractor.setCity(it.second)
                viewState.onRegionCityChanged(it.first, it.second)
            }
            .launchIn(presenterScope)
    }

    fun openActivityTypeSelectionScreen() {
        branchUpdateInteractor.getActivityType().let {
            branchSavingFeatureCallback.onOpenActivityTypeSelection(it)
        }
    }

    fun openRegionSelectionScreen() {
        branchUpdateInteractor.getRegionCityPair().let {
            branchSavingFeatureCallback
                .onOpenRegionCitySelection(it.first, it.second, RegionCitySelectionType.REGION)
        }
    }

    fun openCitySelectionScreen() {
        branchUpdateInteractor.getRegionCityPair().let {
            branchSavingFeatureCallback
                .onOpenRegionCitySelection(it.first, it.second, RegionCitySelectionType.CITY)
        }
    }

    fun backToRootScreen() =
        branchSavingFeatureCallback.onBackFromBranchSaving()
}