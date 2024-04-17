package uz.uzkassa.smartpos.feature.branch.saving.presentation.creation

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
import uz.uzkassa.smartpos.feature.branch.saving.domain.creation.BranchCreationInteractor
import javax.inject.Inject

internal class BranchCreationPresenter @Inject constructor(
    private val activityTypeLazyFlow: Lazy<Flow<ActivityType>>,
    private val branchCreationInteractor: BranchCreationInteractor,
    private val branchSavingFeatureCallback: BranchSavingFeatureCallback,
    private val regionCityPairLazyFlow: Lazy<Flow<Pair<Region, City>>>
) : MvpPresenter<BranchCreationView>() {

    override fun onFirstViewAttach() {
        getActivityType()
        getRegionCityPair()
    }

    fun removeActivityType(activityType: ActivityType) {
        branchCreationInteractor.removeActivityType()
        viewState.onActivityTypeRemoved(activityType)
    }

    fun setName(value: String) =
        branchCreationInteractor.setName(value)

    fun setAddress(value: String) =
        branchCreationInteractor.setAddress(value)

    fun createBranch() {
        branchCreationInteractor
            .createBranch()
            .launchCatchingIn(presenterScope)
            .onStart { viewState.onLoadingCreation() }
            .onSuccess { branchSavingFeatureCallback.onFinishBranchSaving(it.id) }
            .onFailure {
                if (it is BranchSavingException) {

                    if (it.isActivityTypeNotDefined)
                        viewState.onErrorCreation(it)

                    if (it.isNameNotDefined)
                        viewState.onErrorCreationCauseNameNotDefined()

                    if (it.isRegionNotDefined)
                        viewState.onErrorCreationCauseRegionNotDefined()

                    if (it.isCityNotDefined)
                        viewState.onErrorCreationCauseCityNotDefined()

                    if (it.isAddressNotDefined)
                        viewState.onErrorCreationCauseAddressNotDefined()

                } else viewState.onErrorCreation(it)
            }
    }

    private fun getActivityType() {
        activityTypeLazyFlow.get()
            .onEach {
                branchCreationInteractor.setActivityType(it)
                viewState.onActivityTypeAdded(it)
            }
            .launchIn(presenterScope)
    }

    private fun getRegionCityPair() {
        regionCityPairLazyFlow.get()
            .onEach {
                branchCreationInteractor.setRegion(it.first)
                branchCreationInteractor.setCity(it.second)
                viewState.onRegionCityChanged(it.first, it.second)
            }
            .launchIn(presenterScope)
    }

    fun openActivityTypeSelectionScreen() {
        branchCreationInteractor.getActivityType().let {
            branchSavingFeatureCallback.onOpenActivityTypeSelection(it)
        }
    }

    fun openRegionSelectionScreen() {
        branchCreationInteractor.getRegionCityPair().let {
            branchSavingFeatureCallback
                .onOpenRegionCitySelection(it.first, it.second, RegionCitySelectionType.REGION)
        }
    }

    fun openCitySelectionScreen() {
        branchCreationInteractor.getRegionCityPair().let {
            branchSavingFeatureCallback
                .onOpenRegionCitySelection(it.first, it.second, RegionCitySelectionType.CITY)
        }
    }

    fun backToRootScreen() =
        branchSavingFeatureCallback.onBackFromBranchSaving()
}