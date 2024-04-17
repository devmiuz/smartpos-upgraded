package uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.service

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.model.ProductPackageTypeResponse

internal class ProductPackageTypeRestServiceImpl(
    private val productPackageTypeRestServiceInternal: ProductPackageTypeRestServiceInternal
) : ProductPackageTypeRestService {

    override fun getProductPackageTypes(): Flow<List<ProductPackageTypeResponse>> {
        return productPackageTypeRestServiceInternal.getProductPackageTypes()
    }

}