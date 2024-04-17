package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.service.Service
import javax.inject.Inject

internal class ServiceRepositoryImpl @Inject constructor() :
    ServiceRepository {

    override fun getServices(): Flow<List<Service>> {
        return flowOf(
            listOf(
                Service.RECEIPT_DRAFT,
                Service.REFUND,
                Service.CASH_OPERATIONS,
                Service.GTPOS,
                Service.ADVANCE_CREDIT
            )
        )
    }
}