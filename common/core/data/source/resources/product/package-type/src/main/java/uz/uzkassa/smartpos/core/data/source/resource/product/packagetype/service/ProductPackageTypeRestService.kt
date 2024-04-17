package uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.service

import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.create
import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.model.ProductPackageTypeResponse

interface ProductPackageTypeRestService {

    fun getProductPackageTypes(): Flow<List<ProductPackageTypeResponse>>

    companion object {

        fun instantiate(retrofit: Retrofit): ProductPackageTypeRestService =
            ProductPackageTypeRestServiceImpl(retrofit.create())
    }
}