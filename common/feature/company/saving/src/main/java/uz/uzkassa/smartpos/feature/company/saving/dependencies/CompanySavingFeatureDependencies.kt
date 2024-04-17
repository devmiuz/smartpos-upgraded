package uz.uzkassa.smartpos.feature.company.saving.dependencies

import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.dao.CompanyBusinessTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.service.CompanyBusinessTypeRestService
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.service.CompanyRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager

interface CompanySavingFeatureDependencies {

    val companyBusinessTypeEntityDao: CompanyBusinessTypeEntityDao

    val companyBusinessTypeRestService: CompanyBusinessTypeRestService

    val companyEntityDao: CompanyEntityDao

    val companyRestService: CompanyRestService

    val companySavingFeatureArgs: CompanySavingFeatureArgs

    val companySavingFeatureCallback: CompanySavingFeatureCallback

    val coroutineContextManager: CoroutineContextManager
}