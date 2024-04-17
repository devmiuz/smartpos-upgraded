package uz.uzkassa.smartpos.feature.receipt.sync.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.receipt.sync.data.repository.ReceiptSyncRepository
import uz.uzkassa.smartpos.feature.receipt.sync.data.repository.ReceiptSyncRepositoryImpl
import uz.uzkassa.smartpos.feature.receipt.sync.domain.ReceiptSyncInteractor

class ReceiptSyncWorker internal constructor(
    context: Context,
    private val receiptSyncInteractor: ReceiptSyncInteractor,
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
                receiptSyncInteractor.syncReceipts()
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
            receiptEntityDao: ReceiptEntityDao,
            receiptRelationDao: ReceiptRelationDao,
            receiptRestService: ReceiptRestService,
            workerParams: WorkerParameters
        ): ReceiptSyncWorker {
            val receiptSyncRepository: ReceiptSyncRepository =
                ReceiptSyncRepositoryImpl(receiptEntityDao, receiptRelationDao, receiptRestService)
            val receiptSyncInteractor = ReceiptSyncInteractor(
                coroutineContextManager = coroutineContextManager,
                receiptSyncRepository = receiptSyncRepository
            )

            return ReceiptSyncWorker(context, receiptSyncInteractor, workerParams)
        }
    }
}