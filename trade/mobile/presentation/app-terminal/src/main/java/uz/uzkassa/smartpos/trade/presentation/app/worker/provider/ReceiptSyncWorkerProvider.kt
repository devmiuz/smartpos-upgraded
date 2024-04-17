package uz.uzkassa.smartpos.trade.presentation.app.worker.provider

import android.content.Context
import androidx.work.WorkerParameters
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptEntityDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.dao.ReceiptRelationDao
import uz.uzkassa.smartpos.core.data.source.resource.receipt.service.ReceiptRestService
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.receipt.sync.worker.ReceiptSyncWorker
import uz.uzkassa.smartpos.trade.presentation.support.dagger2.worker.WorkerProvider
import javax.inject.Inject

class ReceiptSyncWorkerProvider @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val receiptEntityDao: ReceiptEntityDao,
    private val receiptRelationDao: ReceiptRelationDao,
    private val receiptRestService: ReceiptRestService
) : WorkerProvider {

    override fun provideWorker(context: Context, workerParams: WorkerParameters) =
        ReceiptSyncWorker.instantiate(
            coroutineContextManager = coroutineContextManager,
            context = context,
            receiptEntityDao = receiptEntityDao,
            receiptRelationDao = receiptRelationDao,
            receiptRestService = receiptRestService,
            workerParams = workerParams
        )
}