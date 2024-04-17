package uz.uzkassa.smartpos.feature.company.saving.domain.creation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessType
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVAT
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.HttpException
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.company.saving.data.exception.CompanyAlreadyCreatedException
import uz.uzkassa.smartpos.feature.company.saving.data.exception.CompanyCreationException
import uz.uzkassa.smartpos.feature.company.saving.data.repository.saving.CompanySavingRepository
import uz.uzkassa.smartpos.feature.company.saving.data.repository.saving.params.SaveCompanyParams
import java.net.HttpURLConnection.HTTP_CONFLICT
import javax.inject.Inject

internal class CompanyCreationInteractor @Inject constructor(
    private val companySavingRepository: CompanySavingRepository,
    private val coroutineContextManager: CoroutineContextManager
) {
    private val activityTypes: MutableList<ActivityType> = arrayListOf()
    private var companyBusinessType: CompanyBusinessType? = null
    private var ownerFirstName: String? = null
    private var ownerLastName: String? = null
    private var city: City? = null
    private var region: Region? = null
    private var companyVAT: CompanyVAT? = null
    private var name: String = ""
    private var address: String = ""

    fun getActivityTypes(): List<ActivityType> =
        activityTypes

    fun getRegionCityPair(): Pair<Region?, City?> =
        Pair(region, city)

    fun getCompanyVAT(): CompanyVAT? =
        companyVAT

    fun setOwnerFirstName(value: String) {
        ownerFirstName = if (value.isNotEmpty()) value else null
    }

    fun setOwnerLastName(value: String) {
        ownerLastName = if (value.isNotEmpty()) value else null
    }

    fun setActivityTypes(list: List<ActivityType>) {
        activityTypes.apply { clear(); addAll(list) }
    }

    fun removeActivityType(activityType: ActivityType) {
        activityTypes.remove(activityType)
    }

    fun setCompanyBusinessType(businessType: CompanyBusinessType) {
        companyBusinessType = businessType
    }

    fun setCity(value: City) {
        city = value
    }

    fun setRegion(value: Region) {
        region = value
    }

    fun setCompanyVAT(value: CompanyVAT?) {
        companyVAT = value
    }

    fun setName(value: String) {
        name = value
    }

    fun setAddress(value: String) {
        address = value
    }

    fun createOwner(): Flow<Result<Unit>> {
        val companyCreationException =
            CompanyCreationException(
                isActivityTypesNotDefined = activityTypes.isEmpty(),
                isOwnerLastNameNotDefined = ownerFirstName.isNullOrEmpty(),
                isOwnerFirstNameNotDefined = ownerLastName.isNullOrEmpty(),
                isCompanyBusinessTypeNotDefined = companyBusinessType == null,
                isNameNotDefined = name.isEmpty(),
                isAddressNotDefined = address.isEmpty(),
                isRegionNotDefined = region == null,
                isCityNotDefined = city == null
            )

        return when {
            companyCreationException.isPassed ->
                flowOf(Result.failure(companyCreationException))
            else ->
                companySavingRepository
                    .createCompany(
                        SaveCompanyParams(
                            ownerFirstName = checkNotNull(ownerFirstName),
                            ownerLastName = checkNotNull(ownerLastName),
                            ownerPatronymic = null,
                            activityTypes = activityTypes,
                            companyBusinessType = checkNotNull(companyBusinessType),
                            city = checkNotNull(city),
                            region = checkNotNull(region),
                            vatPercent = companyVAT?.percent,
                            tin = null,
                            name = name,
                            address = address
                        )
                    )
                    .catch {
                        throw if (it is HttpException && it.response.httpErrorCode == HTTP_CONFLICT)
                            CompanyAlreadyCreatedException()
                        else it
                    }
                    .flatMapResult()
                    .flowOn(coroutineContextManager.ioContext)
        }
    }
}