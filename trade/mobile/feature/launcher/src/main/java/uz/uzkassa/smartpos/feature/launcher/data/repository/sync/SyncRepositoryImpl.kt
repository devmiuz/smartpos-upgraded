package uz.uzkassa.smartpos.feature.launcher.data.repository.sync

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.preference.cleaner.PreferenceCleaner
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.dao.ActivityTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.activitytype.service.ActivityTypeRestService
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.service.BranchRestService
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.core.data.source.resource.city.dao.CityEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.city.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.dao.CompanyBusinessTypeEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.company.businesstype.service.CompanyBusinessTypeRestService
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.company.service.CompanyRestService
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.service.ProductRestService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.dao.ReceiptTemplateEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.service.ReceiptTemplateRestService
import uz.uzkassa.smartpos.core.data.source.resource.region.dao.RegionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.region.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.NotFoundHttpException
import uz.uzkassa.smartpos.core.data.source.resource.unit.dao.UnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.unit.service.UnitRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.dao.UserRoleEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.user.role.service.UserRoleRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import uz.uzkassa.smartpos.feature.launcher.data.exception.CompanyNotFoundException
import uz.uzkassa.smartpos.feature.launcher.data.exception.CurrentBranchNotDefinedException
import uz.uzkassa.smartpos.feature.launcher.data.model.sync.SyncState
import uz.uzkassa.smartpos.feature.launcher.data.preference.branch.CurrentBranchPreference
import uz.uzkassa.smartpos.feature.launcher.data.preference.state.LauncherStatePreference
import javax.inject.Inject

internal class SyncRepositoryImpl @Inject constructor(
    private val activityTypeEntityDao: ActivityTypeEntityDao,
    private val activityTypeRestService: ActivityTypeRestService,
    private val branchEntityDao: BranchEntityDao,
    private val branchRestService: BranchRestService,
    private val categoryEntityDao: CategoryEntityDao,
    private val categoryRestService: CategoryRestService,
    private val cityEntityDao: CityEntityDao,
    private val currentBranchPreference: CurrentBranchPreference,
    private val companyBusinessTypeEntityDao: CompanyBusinessTypeEntityDao,
    private val companyBusinessTypeRestService: CompanyBusinessTypeRestService,
    private val companyEntityDao: CompanyEntityDao,
    private val companyRestService: CompanyRestService,
    private val launcherStatePreference: LauncherStatePreference,
    private val productEntityDao: ProductEntityDao,
    private val productRestService: ProductRestService,
    private val receiptTemplateEntityDao: ReceiptTemplateEntityDao,
    private val receiptTemplateRestService: ReceiptTemplateRestService,
    private val regionEntityDao: RegionEntityDao,
    private val unitEntityDao: UnitEntityDao,
    private val unitRestService: UnitRestService,
    private val userEntityDao: UserEntityDao,
    private val userRestService: UserRestService,
    private val userRoleEntityDao: UserRoleEntityDao,
    private val userRoleRestService: UserRoleRestService,
    private val preferenceCleaner: PreferenceCleaner
    ) : SyncRepository {
    private var syncState: SyncState = SyncState()

    @FlowPreview
    override fun getSyncState(): Flow<SyncState> {
        return getActivityTypes()
            .flatMapConcat { getCompanyBusinessTypes() }
            .flatMapConcat { getUnits() }
            .flatMapConcat { getUserRoles() }
            .flatMapConcat { getCurrentCompany() }
            .flatMapConcat { if (it) getReceiptTemplate() else flowOf(it) }
            .flatMapConcat { if (it) getBranches() else flowOf(it) }
            .flatMapConcat { if (it) getEnabledCategoriesByLastModifiedDate() else flowOf(it) }
            .flatMapConcat { if (it) getProductsByLastModifiedDate() else flowOf(it) }
            .map { syncState }
    }

    override fun clearAppDataAndLogout(): Flow<Unit> {
        return flowOf(Unit)
            .onEach {
                branchEntityDao.clearAllTables()
                preferenceCleaner.clearAll()
            }
    }

    private fun getActivityTypes(): Flow<Unit> {
        return activityTypeRestService
            .getActivityTypes()
            .onEach {
                activityTypeEntityDao.upsert(it.mapToEntities())
            }
            .map { }
    }

    @FlowPreview
    private fun getBranches(): Flow<Boolean> {
        return flowOf(currentBranchPreference.branchId)
            .flatMapConcat {
                return@flatMapConcat if (it != null) {
                    branchRestService.getBranchById(it)
                } else {
                    branchRestService.getCurrentBranch()
                }
            }
            .onEach {
                branchEntityDao.save(it)
            }
            .onEach {
                currentBranchPreference.branchId = it.id
            }
            .catch { throw if (it is NotFoundHttpException) CurrentBranchNotDefinedException() else it }
            .flatMapConcat { response ->
                userRestService
                    .getUsersByBranchId(response.id)
                    .onEach { userEntityDao.save(it) }
                    .onEach {
                        val isUsersCreated: Boolean =
                            it.size >= 2 || !launcherStatePreference.isUsersNotDefined

                        syncState = syncState.copy(isUsersCreated = isUsersCreated)
                    }
                    .map { true }
            }
            .map { it }
            .onEach { syncState = syncState.copy(isBranchesCreated = it) }
    }

    private fun getCompanyBusinessTypes(): Flow<Unit> {
        return companyBusinessTypeRestService
            .getCompanyBusinessTypes()
            .onEach { companyBusinessTypeEntityDao.upsert(it.mapToEntities()) }
            .map { Unit }
    }

    private fun getCurrentCompany(): Flow<Boolean> {
        return companyRestService
            .getCurrentCompany()
            .onEach { companyEntityDao.deleteAll() }
            .onEach { activityTypeEntityDao.upsert(it.activityType.mapToEntities()) }
            .onEach { cityEntityDao.upsert(it.city.mapToEntity()) }
            .onEach { companyBusinessTypeEntityDao.upsert(it.companyBusinessType.mapToEntity()) }
            .onEach { companyEntityDao.upsert(it.mapToEntity()) }
            .onEach { regionEntityDao.upsert(it.region.mapToEntity()) }
            .map { true }
            .catch { throw if (it is NotFoundHttpException) CompanyNotFoundException() else it }
            .onEach { syncState = syncState.copy(isCompanyCreated = it) }
    }

    @FlowPreview
    private fun getEnabledCategoriesByLastModifiedDate(): Flow<Boolean> {
        return flowOf(currentBranchPreference.branchId)
            .filterNotNull()
            .flatMapConcat { it ->
                return@flatMapConcat when (launcherStatePreference.isCategoriesNotDefined) {
                    true ->
                        categoryRestService
                            .getEnabledCategoriesByBranchId(it)
                            .onEach { categoryEntityDao.save(it) }
                            .map { it.isNotEmpty() }
                            .onEach { launcherStatePreference.isCategoriesNotDefined = !it }
                    else ->
                        categoryRestService
                            .getEnabledCategoriesByLastModifiedDate(it)
                            .onEach { categoryEntityDao.save(it) }
                            .map { true }
                }
            }
            .onEach { syncState = syncState.copy(isCategoriesDefined = it) }
    }

    @FlowPreview
    private fun getProductsByLastModifiedDate(): Flow<Boolean> {
        return flowOf(currentBranchPreference.branchId)
            .filterNotNull()
            .flatMapConcat { productRestService.getProductsByLastModifiedDate(it) }
            .onEach {
                productEntityDao.save(it)
            }
            .map { true }
    }

    private fun getUnits(): Flow<Unit> {
        return unitRestService
            .getUnits()
            .onEach { unitEntityDao.upsert(it.mapToEntities()) }
            .map { Unit }
    }

    private fun getReceiptTemplate(): Flow<Boolean> {
        return receiptTemplateRestService
            .getReceiptTemplate()
            .onEach { receiptTemplateEntityDao.save(it) }
            .map { true }
            .catch {
                if (it is NotFoundHttpException) {
                    receiptTemplateEntityDao.deleteAll()
                    emit(true)
                } else throw it
            }
    }

    private fun getUserRoles(): Flow<Unit> {
        return userRoleRestService
            .getUserRoles()
            .onEach { userRoleEntityDao.upsert(it.mapToEntities()) }
            .map { Unit }
    }
}