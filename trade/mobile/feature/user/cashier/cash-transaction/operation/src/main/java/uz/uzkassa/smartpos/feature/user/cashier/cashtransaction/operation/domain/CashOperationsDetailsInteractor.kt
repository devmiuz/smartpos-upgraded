package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.core.utils.extensions.flatMapResult
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.model.details.CashOperationsDetails
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.operation.data.repository.details.CashOperationsDetailsRepository
import javax.inject.Inject

internal class CashOperationsDetailsInteractor @Inject constructor(
    private val cashOperationsDetailsRepository: CashOperationsDetailsRepository,
    private val coroutineContextManager: CoroutineContextManager
){

    fun getCashOperationsDetails():Flow<Result<CashOperationsDetails>>{
        return cashOperationsDetailsRepository.getCashOperationsDetails()
            .flatMapResult()
            .flowOn(coroutineContextManager.ioContext)
    }
}