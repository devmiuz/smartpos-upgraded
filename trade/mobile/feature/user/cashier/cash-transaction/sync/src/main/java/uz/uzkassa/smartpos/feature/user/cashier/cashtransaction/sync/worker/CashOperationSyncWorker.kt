package uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.sync.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.dao.CashTransactionEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.cashtransaction.service.CashTransactionRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.sync.data.repository.CashOperationSyncRepository
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.sync.data.repository.CashOperationSyncRepositoryImpl
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.sync.domain.CashOperationSyncInteractor

class CashOperationSyncWorker internal constructor(
    context: Context,
    private val cashOperationSyncInteractor: CashOperationSyncInteractor,
    workerParams: WorkerParameters
) : ListenableWorker(context, workerParams) {
    private val serviceJob = CoroutineScope(context = Dispatchers.Main + Job())

    override fun startWork(): ListenableFuture<Result> =
        coroutineWorker.startWork()

    override fun onStopped() {
        coroutineWorker.onStopped()
        serviceJob.coroutineContext.cancel()
    }

    private val coroutineWorker: CoroutineWorker = object : CoroutineWorker(context, workerParams) {
        override suspend fun doWork(): Result {
            return withContext(serviceJob.coroutineContext) {
                cashOperationSyncInteractor.syncCashOperations()
                    .map { Result.success() }
                    .catch { emit(Result.success()) }
                    .single()
            }
        }
    }

    companion object {

        fun instantiate(
            coroutineContextManager: CoroutineContextManager,
            context: Context,
            cashTransactionEntityDao: CashTransactionEntityDao,
            cashTransactionRestService: CashTransactionRestService,
            workerParams: WorkerParameters
        ): CashOperationSyncWorker {
            val cashOperationSyncRepository: CashOperationSyncRepository =
                CashOperationSyncRepositoryImpl(
                    cashTransactionEntityDao,
                    cashTransactionRestService
                )
            val receiptSyncInteractor = CashOperationSyncInteractor(
                coroutineContextManager = coroutineContextManager,
                cashOperationSyncRepository = cashOperationSyncRepository
            )

            return CashOperationSyncWorker(context, receiptSyncInteractor, workerParams)
        }
    }
}