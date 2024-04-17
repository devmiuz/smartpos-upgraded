package uz.uzkassa.smartpos.core.data.source.resource.category.service

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import retrofit2.http.*
import uz.uzkassa.smartpos.core.data.source.resource.category.model.CategoryResponse

internal interface CategoryRestServiceInternal {

    @POST(API_CATEGORIES)
    fun createCategory(@Body jsonElement: JsonElement): Flow<CategoryResponse>

    @GET(API_CATEGORIES_ENABLED)
    fun getEnabledCategoriesByBranchId(
        @Query(QUERY_BRANCH_ID) branchId: Long
    ): Flow<List<CategoryResponse>>

    @GET(API_CATEGORIES_MAIN)
    fun getMainCategoriesByBranchId(
        @Query(QUERY_BRANCH_ID) branchId: Long
    ): Flow<List<CategoryResponse>>

    @GET(API_CATEGORIES_MAIN)
    fun getMainCategoriesByLastModifiedDate(
        @Query(QUERY_BRANCH_ID) branchId: Long,
        @Query(QUERY_LAST_MODIFIED_DATE) lastModifiedDate: String
    ): Flow<List<CategoryResponse>>

    @PUT(API_CATEGORIES_LOAD_FROM_CATALOG)
    fun setEnabledCategories(
        @Query(QUERY_BRANCH_ID) branchId: Long,
        @Body jsonElement: JsonElement
    ): Flow<Unit>

    private companion object {
        const val API_CATEGORIES: String = "api/categories"
        const val API_CATEGORIES_ENABLED: String = "api/categories/enabled"
        const val API_CATEGORIES_LOAD_FROM_CATALOG: String = "api/categories/load-from-catalog"
        const val API_CATEGORIES_MAIN: String = "api/categories/main"
        const val QUERY_BRANCH_ID: String = "branchId"
        const val QUERY_LAST_MODIFIED_DATE: String = "lastModifiedDate"
    }
}