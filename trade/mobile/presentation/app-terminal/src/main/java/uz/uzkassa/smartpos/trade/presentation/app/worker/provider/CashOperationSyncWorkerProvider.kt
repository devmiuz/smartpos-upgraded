package uz.uzkassa.smartpos.trade.presentation.app.worker.provider

import android.content.Context
import androidx.work.WorkerParameters
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.dao.CashTransactionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.service.CashTransactionRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.sync.worker.CashOperationSyncWorker
import uz.uzkassa.smartpos.trade.presentation.support.dagger2.worker.WorkerProvider
import javax.inject.Inject

class CashOperationSyncWorkerProvider @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val cashTransactionEntityDao: CashTransactionEntityDao,
    private val cashTransactionRestService: CashTransactionRestService
) : WorkerProvider {

    override fun provideWorker(
        context: Context,
        workerParams: WorkerParameters
    ) =
        CashOperationSyncWorker.instantiate(
            coroutineContextManager = coroutineContextManager,
            context = context,
            cashTransactionEntityDao = cashTransactionEntityDao,
            cashTransactionRestService = cashTransactionRestService,
            workerParams = workerParams
        )
}