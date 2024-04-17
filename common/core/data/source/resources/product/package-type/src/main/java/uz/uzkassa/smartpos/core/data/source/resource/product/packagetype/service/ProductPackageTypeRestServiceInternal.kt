package uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.service

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.model.ProductPackageTypeResponse

internal interface ProductPackageTypeRestServiceInternal {

    @GET(API_PRODUCT_PACKAGE_TYPES)
    fun getProductPackageTypes(): Flow<List<ProductPackageTypeResponse>>
    
    private companion object {
        const val API_PRODUCT_PACKAGE_TYPES: String = "api/product/package-types"
    }
}