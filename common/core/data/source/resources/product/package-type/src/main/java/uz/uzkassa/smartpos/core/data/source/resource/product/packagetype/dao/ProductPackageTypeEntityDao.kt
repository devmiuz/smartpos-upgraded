package uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import uz.uzkassa.smartpos.core.data.source.resource.product.packagetype.model.ProductPackageTypeEntity

@Dao
abstract class ProductPackageTypeEntityDao : BaseDao.Impl<ProductPackageTypeEntity>() {

    @Query(value = "SELECT * FROM product_package_types")
    abstract fun getEntities(): Flow<List<ProductPackageTypeEntity>>
}