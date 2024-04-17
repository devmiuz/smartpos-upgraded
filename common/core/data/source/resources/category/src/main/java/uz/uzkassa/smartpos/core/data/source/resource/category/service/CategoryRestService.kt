package uz.uzkassa.smartpos.core.data.source.resource.category.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.Body
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryResponse
import uz.uzkassa.smartpos.core.data.source.resource.category.preference.CategorySyncTimePreference

interface CategoryRestService {

    fun createCategory(@Body jsonElement: JsonElement): Flow<CategoryResponse>

    fun getEnabledCategoriesByBranchId(branchId: Long): Flow<List<CategoryResponse>>

    fun getEnabledCategoriesByLastModifiedDate(branchId: Long): Flow<List<CategoryResponse>>

    fun getMainCategoriesByBranchId(
        branchId: Long,
        lastModifiedDate: String? = null
    ): Flow<List<CategoryResponse>>

    fun setEnabledCategories(branchId: Long, jsonElement: JsonElement): Flow<Unit>

    companion object {

        fun instantiate(
            retrofit: Retrofit,
            syncTimePreference: CategorySyncTimePreference
        ): CategoryRestService =
            CategoryRestServiceImpl(retrofit.create(), syncTimePreference)
    }
}