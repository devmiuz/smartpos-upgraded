package uz.uzkassa.smartpos.core.data.source.resource.category.service

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonElement
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryResponse
import uz.uzkassa.smartpos.core.data.source.resource.category.preference.CategorySyncTimePreference
import java.util.*

internal class CategoryRestServiceImpl(
    private val restServiceInternal: CategoryRestServiceInternal,
    private val syncTimePreference: CategorySyncTimePreference
) : CategoryRestService {

    override fun createCategory(jsonElement: JsonElement): Flow<CategoryResponse> {
        return restServiceInternal.createCategory(jsonElement)
    }

    override fun getEnabledCategoriesByBranchId(branchId: Long): Flow<List<CategoryResponse>> {
        return restServiceInternal.getEnabledCategoriesByBranchId(branchId)
    }

    @FlowPreview
    override fun getEnabledCategoriesByLastModifiedDate(branchId: Long): Flow<List<CategoryResponse>> {
        return flowOf(syncTimePreference.lastSyncTime)
            .flatMapConcat { it ->
                return@flatMapConcat if (it == null) getEnabledCategoriesByBranchId(branchId)
                else restServiceInternal.getMainCategoriesByLastModifiedDate(branchId, it)
                    .catch {
                        if (it !is SerializationException)
                            emitAll(getEnabledCategoriesByBranchId(branchId))
                        else throw it
                    }
            }
            .onEach { syncTimePreference.setLastSyncTime(Date()) }
    }

    override fun getMainCategoriesByBranchId(
        branchId: Long,
        lastModifiedDate: String?
    ): Flow<List<CategoryResponse>> {
        return if (lastModifiedDate == null)
            restServiceInternal.getMainCategoriesByBranchId(branchId)
        else restServiceInternal.getMainCategoriesByLastModifiedDate(branchId, lastModifiedDate)
    }

    override fun setEnabledCategories(branchId: Long, jsonElement: JsonElement): Flow<Unit> {
        return restServiceInternal.setEnabledCategories(branchId, jsonElement)
    }
}