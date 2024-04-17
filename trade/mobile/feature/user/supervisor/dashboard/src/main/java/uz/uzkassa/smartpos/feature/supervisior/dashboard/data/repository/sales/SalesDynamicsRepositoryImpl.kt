package uz.uzkassa.smartpos.feature.supervisior.dashboard.data.repository.sales

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.dao.SalesDynamicsEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.mapper.map
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.mapper.mapToEntity
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.model.SalesDynamics
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.model.SalesDynamicsEntity
import uz.uzkassa.smartpos.core.data.source.resource.analytics.sales.service.SalesDynamicsRestService
import uz.uzkassa.smartpos.core.data.source.resource.receipt.model.receipt.status.ReceiptStatus
import uz.uzkassa.smartpos.core.utils.util.toString
import uz.uzkassa.smartpos.core.utils.util.withoutTime
import uz.uzkassa.smartpos.feature.analytics.sales.dynamics.data.model.granularity.Granularity
import uz.uzkassa.smartpos.feature.supervisior.dashboard.data.dao.ReceiptTotalDao
import uz.uzkassa.smartpos.feature.supervisior.dashboard.data.mapper.mapToSalesDynamicsEntity
import uz.uzkassa.smartpos.feature.supervisior.dashboard.dependencies.SupervisorDashboardFeatureArgs
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

internal class SalesDynamicsRepositoryImpl @Inject constructor(
    private val receiptTotalDao: ReceiptTotalDao,
    private val restService: SalesDynamicsRestService,
    private val salesDynamicsEntityDao: SalesDynamicsEntityDao,
    private val supervisorDashboardFeatureArgs: SupervisorDashboardFeatureArgs
) : SalesDynamicsRepository {
    private val dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss"

    /**
     * сначала будет получит из сервера и сохраняет
     * при сохранение заменяет базу
     *
     * потом получает из не синхронних чеки и обновляет базу
     * */
    @FlowPreview
    override fun getSalesDynamicsByFilter(
        fromDate: Date,
        toDate: Date,
        granularity: Granularity
    ): Flow<List<SalesDynamics>> {
        return flowOf(supervisorDashboardFeatureArgs.branchId)
            .filterNotNull()
            .flatMapConcat {
                restService
                    .getSalesDynamics(
                        branchId = supervisorDashboardFeatureArgs.branchId,
                        fromDate = fromDate.toString(dateFormat),
                        toDate = toDate.toString(dateFormat),
                        granularity = granularity.name.toLowerCase(Locale.ROOT)
                    )
            }
            .onEach { list -> list.forEach { salesDynamicsEntityDao.upsert(it.mapToEntity()) } }
            .flatMapConcat {
                val refunds =
                    receiptTotalDao.getReceiptTotalDetailsByStatus(ReceiptStatus.RETURNED.name)
                val sales = receiptTotalDao.getReceiptTotalDetailsByStatus(ReceiptStatus.PAID.name)
                sales.forEach { sale ->
                    val refund = refunds.find { it.date.withoutTime() == sale.date.withoutTime() }
                        ?.total ?: BigDecimal.ZERO
                    val receiptDynamicsEntity: SalesDynamicsEntity =
                        sale.mapToSalesDynamicsEntity(refund)

                    val dynamicsEntity: SalesDynamicsEntity? =
                        salesDynamicsEntityDao.getSalesDynamicsByDate(sale.date.withoutTime())
                            ?.let { updateSalesDynamicsEntity(it, receiptDynamicsEntity) }

                    if (dynamicsEntity != null) salesDynamicsEntityDao.upsert(dynamicsEntity)
                    else salesDynamicsEntityDao.upsert(receiptDynamicsEntity)
                }

                flowOf(Unit)
            }
            .flatMapConcat {
                getSalesDynamicsByDateRange(fromDate, toDate)
                    .map { list -> list.map { it.map() } }
            }
    }

    override fun getSalesDynamicsByDate(date: Date): Flow<SalesDynamics?> {
        return flow { emit(salesDynamicsEntityDao.getSalesDynamicsByDate(date)) }.map { it?.map() }
    }

    private fun getSalesDynamicsByDateRange(
        fromDate: Date,
        toDate: Date
    ): Flow<List<SalesDynamicsEntity>> {
        return flow {
            val entities: List<SalesDynamicsEntity> =
                salesDynamicsEntityDao.getSalesDynamicsByDateRange(fromDate, toDate).first()
            emit(entities)
        }
    }

    private fun updateSalesDynamicsEntity(
        current: SalesDynamicsEntity,
        other: SalesDynamicsEntity
    ): SalesDynamicsEntity =
        SalesDynamicsEntity(
            salesDate = current.salesDate,
            salesCount = current.salesCount + other.salesCount,
            salesCash = current.salesCash + other.salesCash,
            salesCard = current.salesCard + other.salesCard,
            discount = current.discount + other.discount,
            refund = current.refund + other.refund,
            vat = current.vat + other.vat,
            salesTotal = current.salesTotal + other.salesTotal,
            averageReceiptCost = current.averageReceiptCost + other.averageReceiptCost
        )
}