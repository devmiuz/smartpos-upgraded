package uz.uzkassa.smartpos.feature.company.saving.presentation.creation

import moxy.MvpView
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.model.ActivityType
import uz.uzkassa.smartpos.core.data.source.resource.city.model.City
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.model.CompanyBusinessType
import uz.uzkassa.smartpos.core.data.source.resource.company.vat.model.CompanyVAT
import uz.uzkassa.smartpos.core.data.source.resource.region.model.Region

internal interface CompanyCreationView : MvpView {

    fun onLoadingCompanyBusinessTypes()

    fun onSuccessCompanyBusinessTypes(companyBusinessTypes: List<CompanyBusinessType>)

    fun onErrorCompanyBusinessTypes(throwable: Throwable)

    fun onCompanyBusinessTypeSelected(companyBusinessType: CompanyBusinessType)

    fun onActivityTypesChanged(activityTypes: List<ActivityType>)

    fun onActivityTypeRemoved(activityType: ActivityType)

    fun onRegionCityChanged(region: Region, city: City)

    fun onCompanyVATChanged(companyVAT: CompanyVAT?, isEnabled: Boolean)

    fun onLoadingCreation()

    fun onErrorCreationCauseOwnerLastNameNotDefined()

    fun onErrorCreationCauseOwnerFirstNameNotDefined()

    fun onErrorCreationCauseCompanyBusinessTypeNotDefined()

    fun onErrorCreationCauseNameNotDefined()

    fun onErrorCreationCauseRegionNotDefined()

    fun onErrorCreationCauseCityNotDefined()

    fun onErrorCreationCauseAddressNotDefined()

    fun onErrorCreation(throwable: Throwable)
}