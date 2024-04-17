package uz.uzkassa.smartpos.feature.user.cashier.sale.data.repository.service

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.user.cashier.sale.data.model.service.Service

internal interface ServiceRepository {

    fun getServices(): Flow<List<Service>>
}