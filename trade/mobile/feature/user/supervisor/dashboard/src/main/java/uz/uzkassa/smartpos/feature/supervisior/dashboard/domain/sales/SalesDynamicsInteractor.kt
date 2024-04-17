package uz.uzkassa.smartpos.feature.supervisior.dashboard.domain.sales

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.analytics.sales.dynamics.data.model.granularity.Granularity
import uz.uzkassa.smartpos.feature.supervisior.dashboard.data.repository.sales.SalesDynamicsRepository
import uz.uzkassa.smartpos.feature.supervisior.dashboard.domain.sales.wrapper.SalesDynamicsWrapper
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

internal class SalesDynamicsInteractor @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val salesDynamicsRepository: SalesDynamicsRepository
) {

    fun getSalesDynamics(
        granularity: Granularity
    ): Flow<Result<List<SalesDynamicsWrapper>>> {
        return salesDynamicsRepository
            .getSalesDynamicsByFilter(getStartDate(granularity), getEndDate(), granularity)
            .map { salesDynamics ->
                val maxSalesTotal: BigDecimal =
                    salesDynamics.maxBy { it.salesTotal }?.salesTotal ?: BigDecimal.ZERO

                salesDynamics.map {
                    var percentOfMaxSalesTotal: Float =
                        it.salesTotal.toFloat() / maxSalesTotal.toFloat()
                    percentOfMaxSalesTotal *= 0.8F
                    if (percentOfMaxSalesTotal < 0.01F)
                        percentOfMaxSalesTotal = 0.01F

                    SalesDynamicsWrapper(it, percentOfMaxSalesTotal)
                }
            }
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }

    private fun getStartDate(granularity: Granularity): Date {
        val calendar: Calendar = Calendar.getInstance()
        return when (granularity) {
            Granularity.DAY -> {
                calendar.apply {
                    set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
            }
            Granularity.WEEK -> {
                calendar.apply {
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
            }
            Granularity.MONTH -> {
                calendar.apply {
                    set(Calendar.DAY_OF_YEAR, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
            }
        }.time
    }

    private fun getEndDate(): Date {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.time
    }
}