package uz.uzkassa.smartpos.feature.sync.worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.sync.common.data.repository.SyncRepository
import uz.uzkassa.smartpos.feature.sync.common.model.request.SyncRequest

@SuppressLint("WorkerHasAPublicModifier")
class SyncWorker(
    private val branchId: Long?,
    private val coroutineContextManager: CoroutineContextManager,
    context: Context,
    private val isSyncAllowed: Boolean,
    private val syncRepository: SyncRepository,
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
            return if (isSyncAllowed) withContext(serviceJob.coroutineContext) {
                syncRepository.getSyncState(SyncRequest(branchId))
                    .map {
                        Result.success()
                    }
                    .catch {
                        emit(Result.success())
                    }
                    .flowOn(coroutineContextManager.ioContext)
                    .single()
            } else Result.success()
        }
    }
}