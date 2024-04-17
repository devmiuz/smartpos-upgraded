package uz.uzkassa.smartpos.feature.branch.saving.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.branch.saving.data.exception.BranchSavingException

@Suppress("MemberVisibilityCanBePrivate", "PropertyName")
internal abstract class BranchSaveInteractor {
    protected var _activityType: ActivityType? = null
    protected var _region: Region? = null
    protected var _city: City? = null

    protected var _name: String? = null
    protected var _address: String? = null

    fun getActivityType(): ActivityType? =
        _activityType

    fun getRegionCityPair(): Pair<Region?, City?> =
        Pair(_region, _city)

    fun removeActivityType() {
        _activityType = null
    }

    fun setActivityType(activityType: ActivityType) {
        _activityType = activityType
    }

    fun setName(value: String) {
        _name = value
    }

    fun setRegion(region: Region) {
        _region = region
    }

    fun setCity(city: City) {
        _city = city
    }

    fun setAddress(value: String) {
        _address = value
    }

    protected fun <T> proceedWithResult(
        flow: (ActivityType, name: String, Region, City, address: String) -> Flow<T>
    ): Flow<Result<T>> {

        val branchCreationException = BranchSavingException(
            isActivityTypeNotDefined = _activityType == null,
            isNameNotDefined = _name.isNullOrEmpty(),
            isRegionNotDefined = _region == null,
            isCityNotDefined = _city == null,
            isAddressNotDefined = _address.isNullOrEmpty()
        )

        return when {
            branchCreationException.isPassed ->
                flowOf(Result.failure(branchCreationException))
            else ->
                flow.invoke(
                    checkNotNull(_activityType),
                    checkNotNull(_name),
                    checkNotNull(_region),
                    checkNotNull(_city),
                    checkNotNull(_address)
                ).flatMapResult()
        }
    }
}