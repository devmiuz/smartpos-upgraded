package uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.model.SalesDynamicsEntity
import uz.uzkassa.smartpos.core.data.source.resource.database.BaseDao
import java.util.*

@Dao
abstract class SalesDynamicsEntityDao : BaseDao.Impl<SalesDynamicsEntity>() {

    @Query("SELECT * FROM analytics_sales_dynamics")
    abstract fun getSalesDynamics(): Flow<List<SalesDynamicsEntity>>

    @Query("SELECT * FROM analytics_sales_dynamics WHERE sales_dynamics_date BETWEEN :fromDate AND :toDate ORDER BY sales_dynamics_date ASC ")
    abstract fun getSalesDynamicsByDateRange(
        fromDate: Date,
        toDate: Date
    ): Flow<List<SalesDynamicsEntity>>

    @Query("SELECT * FROM analytics_sales_dynamics WHERE sales_dynamics_date = :date")
    abstract fun getSalesDynamicsByDate(date: Date): SalesDynamicsEntity?

    @Query("DELETE FROM analytics_sales_dynamics WHERE DATE(sales_dynamics_date) = DATE(:date)")
    abstract fun deleteSalesDynamicsByDate(date: Date)
}