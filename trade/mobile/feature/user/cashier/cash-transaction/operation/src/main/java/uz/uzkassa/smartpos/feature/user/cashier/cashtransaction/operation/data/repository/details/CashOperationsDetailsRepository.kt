package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.details

import kotlinx.coroutines.flow.Flow
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.model.details.CashOperationsDetails

internal interface CashOperationsDetailsRepository {

    fun getCashOperationsDetails(): Flow<CashOperationsDetails>
}