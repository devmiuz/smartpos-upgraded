package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.sync

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.branch.dao.BranchEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.branch.service.BranchRestService
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.core.data.source.resource.company.dao.CompanyEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.company.service.CompanyRestService
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.service.ProductRestService
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.dao.ProductUnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.product.unit.model.ProductUnitEntity
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.dao.ReceiptTemplateEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.service.ReceiptTemplateRestService
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.NotFoundHttpException
import uz.uzkassa.smartpos.core.data.source.resource.unit.dao.UnitEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.unit.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.unit.service.UnitRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.dao.UserEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.dao.UserRoleEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.user.role.mapper.mapToEntities
import uz.uzkassa.smartpos.core.data.source.resource.user.role.service.UserRoleRestService
import uz.uzkassa.smartpos.core.data.source.resource.user.service.UserRestService
import javax.inject.Inject

internal class SyncRepositoryImpl @Inject constructor(
    private val branchEntityDao: BranchEntityDao,
    private val branchRestService: BranchRestService,
    private val categoryEntityDao: CategoryEntityDao,
    private val categoryRestService: CategoryRestService,
    private val companyEntityDao: CompanyEntityDao,
    private val companyRestService: CompanyRestService,
    private val productEntityDao: ProductEntityDao,
    private val productRestService: ProductRestService,
    private val productUnitEntityDao: ProductUnitEntityDao,
    private val receiptTemplateEntityDao: ReceiptTemplateEntityDao,
    private val receiptTemplateRestService: ReceiptTemplateRestService,
    private val unitEntityDao: UnitEntityDao,
    private val unitRestService: UnitRestService,
    private val userEntityDao: UserEntityDao,
    private val userRestService: UserRestService,
    private val userRoleEntityDao: UserRoleEntityDao,
    private val userRoleRestService: UserRoleRestService
) : SyncRepository {

    @FlowPreview
    override fun getSyncState(branchId: Long): Flow<Unit> {
        return getUnits()
            .flatMapConcat { getUserRoles() }
            .flatMapConcat { getCurrentCompany() }
            .flatMapConcat { getReceiptTemplate() }
            .flatMapConcat { getBranch(branchId) }
            .flatMapConcat { getEnabledCategoriesByLastModifiedDate(branchId) }
            .flatMapConcat { getProductsByLastModifiedDate(branchId) }
            .map { Unit }
    }

    @Suppress("LABEL_NAME_CLASH")
    @FlowPreview
    private fun getBranch(branchId: Long): Flow<Unit> {
        return flowOf(branchId)
            .flatMapConcat { it ->
                return@flatMapConcat if (it != null)
                    branchRestService.getBranchById(it)
                        .onEach { branchEntityDao.save(it) }
                        .flatMapConcat { response ->
                            userRestService
                                .getUsersByBranchId(response.id)
                                .onEach { list -> userEntityDao.save(list) }
                        }
                        .map { Unit }
                else flowOf(Unit)
            }
    }

    private fun getCurrentCompany(): Flow<Unit> {
        return companyRestService
            .getCurrentCompany()
            .onEach { companyEntityDao.save(it) }
            .map { Unit }
    }

    @FlowPreview
    private fun getEnabledCategoriesByLastModifiedDate(branchId: Long): Flow<Unit> {
        return flowOf(branchId)
            .flatMapConcat { it ->
                return@flatMapConcat if (it == null) flowOf(Unit)
                else categoryRestService
                    .getEnabledCategoriesByLastModifiedDate(it)
                    .onEach { categoryEntityDao.save(it) }
                    .map { Unit }
            }
    }

    @FlowPreview
    private fun getProductsByLastModifiedDate(branchId: Long): Flow<Unit> {
        return flowOf(branchId)
            .flatMapConcat { branchId ->
                return@flatMapConcat if (branchId == null) flowOf(Unit)
                else productRestService.getProductsByLastModifiedDate(branchId)
                    .onEach { it -> categoryEntityDao.save(it.map { it.category }) }
                    .onEach { productEntityDao.save(it) }
                    .onEach { it ->
                        val entities: List<ProductUnitEntity> =
                            it.flatMap { it.productUnits ?: emptyList() }.mapToEntities()
                        if (entities.isEmpty()) return@onEach
                        productUnitEntityDao.save(
                            productIds = it.map { it.id }.toLongArray(),
                            responses = it.flatMap { it.productUnits ?: emptyList() }
                        )
                    }
                    .map { Unit }
            }
    }

    private fun getUnits(): Flow<Unit> {
        return unitRestService
            .getUnits()
            .onEach { unitEntityDao.upsert(it.mapToEntities()) }
            .map { Unit }
    }

    private fun getReceiptTemplate(): Flow<Unit> {
        return receiptTemplateRestService
            .getReceiptTemplate()
            .onEach { receiptTemplateEntityDao.save(it) }
            .map { Unit }
            .catch { if (it is NotFoundHttpException) emit(Unit) else throw it }
    }

    private fun getUserRoles(): Flow<Unit> {
        return userRoleRestService
            .getUserRoles()
            .onEach { userRoleEntityDao.upsert(it.mapToEntities()) }
            .map { Unit }
    }
}