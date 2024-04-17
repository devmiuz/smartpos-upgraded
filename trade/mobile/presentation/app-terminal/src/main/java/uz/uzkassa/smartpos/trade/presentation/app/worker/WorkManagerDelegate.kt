package uz.uzkassa.smartpos.trade.presentation.app.worker

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import uz.uzkassa.smartpos.feature.receipt.sync.worker.ReceiptSyncWorker
import uz.uzkassa.smartpos.feature.sync.worker.SyncWorker
import uz.uzkassa.smartpos.feature.user.cashier.cashtransaction.sync.worker.CashOperationSyncWorker
import java.util.concurrent.TimeUnit

object WorkManagerDelegate {
    private const val CASH_OPERATION_SYNC_WORKER_TAG: String = "cash_operation_sync_worker"
    private const val RECEIPT_SYNC_WORKER_TAG: String = "receipt_sync_worker"
    private const val SYNC_WORKER_TAG: String = "sync_worker"

    fun enqueue(context: Context) {
        WorkManager.getInstance(context).apply {
            enqueueUniquePeriodicWork(
                CASH_OPERATION_SYNC_WORKER_TAG,
                ExistingPeriodicWorkPolicy.REPLACE,
                cashOperationSyncWorkRequest
            )
            enqueueUniquePeriodicWork(
                RECEIPT_SYNC_WORKER_TAG,
                ExistingPeriodicWorkPolicy.REPLACE,
                receiptSyncWorkRequest
            )
            enqueueUniquePeriodicWork(
                SYNC_WORKER_TAG,
                ExistingPeriodicWorkPolicy.REPLACE,
                syncWorkRequest
            )
        }
    }

    private val cashOperationSyncWorkRequest =
        PeriodicWorkRequest.Builder(CashOperationSyncWorker::class.java, 15, TimeUnit.MINUTES)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.MINUTES)
            .build()

    private val receiptSyncWorkRequest =
        PeriodicWorkRequest.Builder(ReceiptSyncWorker::class.java, 15, TimeUnit.MINUTES)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.MINUTES)
            .build()

    private val syncWorkRequest =
        PeriodicWorkRequest.Builder(SyncWorker::class.java, 15, TimeUnit.MINUTES)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.MINUTES)
            .build()
}