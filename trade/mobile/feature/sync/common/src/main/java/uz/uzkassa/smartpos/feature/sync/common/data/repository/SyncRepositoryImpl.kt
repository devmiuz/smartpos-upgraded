package uz.uzkassa.smartpos.feature.sync.common.data.repository

import android.util.Log
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import uz.uzkassa.smartpos.core.data.source.resource.branch.BranchStore
import uz.uzkassa.smartpos.core.data.source.resource.category.dao.CategoryEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryResponse
import uz.uzkassa.smartpos.core.data.source.resource.category.preference.CategorySyncTimePreference
import uz.uzkassa.smartpos.core.data.source.resource.category.service.CategoryRestService
import uz.uzkassa.smartpos.core.data.source.resource.company.CompanyStore
import uz.uzkassa.smartpos.core.data.source.resource.product.dao.ProductEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.product.preference.ProductSyncTimePreference
import uz.uzkassa.smartpos.core.data.source.resource.product.service.ProductRestService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.template.ReceiptTemplateStore
import uz.uzkassa.smartpos.core.data.source.resource.rest.exception.NotFoundHttpException
import uz.uzkassa.smartpos.core.data.source.resource.user.UserStore
import uz.uzkassa.smartpos.feature.sync.common.data.exception.CompanyNotFoundException
import uz.uzkassa.smartpos.feature.sync.common.data.exception.CurrentBranchNotDefinedException
import uz.uzkassa.smartpos.feature.sync.common.model.request.SyncRequest
import uz.uzkassa.smartpos.feature.sync.common.model.result.SyncProgress.BranchProgress
import uz.uzkassa.smartpos.feature.sync.common.model.result.SyncProgress.CategoriesProgress
import uz.uzkassa.smartpos.feature.sync.common.model.result.SyncProgress.CompanyProgress
import uz.uzkassa.smartpos.feature.sync.common.model.result.SyncProgress.ProductsProgress
import uz.uzkassa.smartpos.feature.sync.common.model.result.SyncProgress.ReceiptTemplateProgress
import uz.uzkassa.smartpos.feature.sync.common.model.result.SyncProgress.UsersProgress
import uz.uzkassa.smartpos.feature.sync.common.model.result.SyncResult
import uz.uzkassa.smartpos.feature.sync.common.utils.setBranch
import uz.uzkassa.smartpos.feature.sync.common.utils.setCategories
import uz.uzkassa.smartpos.feature.sync.common.utils.setCompany
import uz.uzkassa.smartpos.feature.sync.common.utils.setProducts
import uz.uzkassa.smartpos.feature.sync.common.utils.setReceiptTemplate
import uz.uzkassa.smartpos.feature.sync.common.utils.setUsers
import java.util.*

@Suppress("EXPERIMENTAL_API_USAGE")
internal class SyncRepositoryImpl(
    private val branchStore: BranchStore,
    private val categoryEntityDao: CategoryEntityDao,
    private val categoryRestService: CategoryRestService,
    private val categorySyncTimePreference: CategorySyncTimePreference,
    private val companyStore: CompanyStore,
    private val productEntityDao: ProductEntityDao,
    private val productRestService: ProductRestService,
    private val productSyncTimePreference: ProductSyncTimePreference,
    private val receiptTemplateStore: ReceiptTemplateStore,
    private val userStore: UserStore
) : SyncRepository {
    private val stateFlow: MutableStateFlow<SyncResult> = MutableStateFlow(SyncResult())

    override fun request(syncRequest: SyncRequest): Flow<SyncResult> {
        syncRequest.syncResult?.let {
            if (stateFlow.value != it)
                stateFlow.value = it
        }
        return flowOf(
            getSyncState(syncRequest)
                .catch {
                    emit(Unit)
                }, stateFlow
        )
            .flattenMerge()
            .filterIsInstance()
    }

    override fun sync(syncRequest: SyncRequest): Flow<Unit> {
        val branchId: Long = syncRequest.branchId ?: throw CurrentBranchNotDefinedException()
        return getCurrentCompany()
            .flatMapConcat { getReceiptTemplate() }
            .flatMapConcat { getBranch(branchId) }
            .flatMapConcat { getUsers(branchId) }
            .flatMapConcat { getCategories(branchId) }
            .flatMapConcat { getProducts(branchId, 0) }
    }

    override fun getSyncState(syncRequest: SyncRequest): Flow<Unit> {
        val flows: MutableList<Flow<Unit>> = arrayListOf()
        val branchId: Long = syncRequest.branchId ?: throw CurrentBranchNotDefinedException()

        if (syncRequest.syncResult?.let { !it.branch.isFinished } != false) {
            Log.wtf("SYNC", "BRANCH")
            flows.add(getBranch(branchId))
        }

        if (syncRequest.syncResult?.let { !it.users.isFinished } != false) {
            Log.wtf("SYNC", "USERS")
            flows.add(getUsers(branchId))
        }

        if (syncRequest.syncResult?.let { !it.categories.isFinished } != false) {
            Log.wtf("SYNC", "CATEGORIES")
            flows.add(getCategories(branchId))
        }

        syncRequest.syncResult?.products.let {
            if (it?.let { !it.isFinished } != false) {
                Log.wtf("SYNC", "PRODUCTS")
                flows.add(getProducts(branchId, (it as? ProductsProgress.Failure)?.page ?: 0))
            }
        }
            return flows.asFlow().flatMapConcat {
            it
        }
//
//        return getCurrentCompany()
//            .flatMapConcat { getCurrentCompany() }
//            .flatMapConcat { getReceiptTemplate() }
//            .flatMapConcat { getBranch(branchId) }
//            .flatMapConcat { getUsers(branchId) }
//            .flatMapConcat { getCategories(branchId) }

//        return flowOf(*flows.toTypedArray())
//            .flattenMerge(1)
//            .onEach { Log.wtf("EACH", "YEAH") }
//            .onCompletion { Log.wtf("ON COMPLETE", "YEAH") }
//            .catch { Log.wtf("SYNC ERROR", it); throw it }
    }

    @FlowPreview
    private fun getBranch(branchId: Long): Flow<Unit> {
        return branchStore.getBranchById().refreshFlow(branchId)
            .onStart { stateFlow.setBranch(BranchProgress.Start) }
            .onEach { stateFlow.setBranch(BranchProgress.Success(it)) }
            .catch { stateFlow.setBranch(BranchProgress.Failure(it)) }
            .map { Unit }
    }

    private fun getCategories(branchId: Long): Flow<Unit> {
        return flowOf(categorySyncTimePreference.lastSyncTime)
            .flatMapConcat { it ->
                val flow: Flow<List<CategoryResponse>> =
                    if (it == null) categoryRestService.getEnabledCategoriesByBranchId(branchId)
                    else categoryRestService.getMainCategoriesByBranchId(branchId, it)

                return@flatMapConcat flow
                    .onStart { stateFlow.setCategories(CategoriesProgress.Start) }
                    .onEach {
                        categoryEntityDao.save(it)
                        categorySyncTimePreference.setLastSyncTime(Date())
                        stateFlow.setCategories(CategoriesProgress.Success)
                    }
                    .catch { stateFlow.setCategories(CategoriesProgress.Failure(it)) }
                    .map { Unit }
            }
    }

    private fun getCurrentCompany(): Flow<Unit> {

        return companyStore.getCurrentCompany().refreshFlow(Unit)
            .onStart { stateFlow.setCompany(CompanyProgress.Start) }
            .onEach { stateFlow.setCompany(CompanyProgress.Success(it)) }
            .catch {
                if (it is NotFoundHttpException) throw CompanyNotFoundException()
                else stateFlow.setCompany(CompanyProgress.Failure(it))
            }
            .map { Unit }
    }

    private fun getReceiptTemplate(): Flow<Unit> {
        return receiptTemplateStore.getReceiptTemplate().refreshFlow(Unit)
            .onStart { stateFlow.setReceiptTemplate(ReceiptTemplateProgress.Start) }
            .onEach { stateFlow.setReceiptTemplate(ReceiptTemplateProgress.Success(it)) }
            .map { Unit }
            .catch { stateFlow.setReceiptTemplate(ReceiptTemplateProgress.Failure(it)) }
    }

    private fun getProducts(branchId: Long, page: Int): Flow<Unit> {
        return productRestService
            .getProductsByBranchId(
                branchId = branchId,
                page = page,
                size = 1000,
                lastModifiedDate = productSyncTimePreference.lastSyncTime
            )
            .onStart { stateFlow.setProducts(ProductsProgress.Start) }
            .onEach {
                productEntityDao.save(it.products)
            }
            .flatMapConcat {
                return@flatMapConcat if (!it.isLast) {
                    stateFlow.setProducts(ProductsProgress.Paging(page, it.totalPages))
                    getProducts(branchId = branchId, page = page + 1)
                } else flowOf(it)
                    .onEach {
                        productSyncTimePreference.setLastSyncTime(Date())
                        stateFlow.setProducts(ProductsProgress.Success)
                    }
            }
            .catch { stateFlow.setProducts(ProductsProgress.Failure(page, it)) }
            .map { Unit }
    }

    @FlowPreview
    private fun getUsers(branchId: Long): Flow<Unit> {
        return userStore.getUsersByBranchId().refreshFlow(branchId)
            .onStart { stateFlow.setUsers(UsersProgress.Start) }
            .onEach { stateFlow.setUsers(UsersProgress.Success(it)) }
            .catch { stateFlow.setUsers(UsersProgress.Failure(it)) }
            .map { Unit }
    }
}