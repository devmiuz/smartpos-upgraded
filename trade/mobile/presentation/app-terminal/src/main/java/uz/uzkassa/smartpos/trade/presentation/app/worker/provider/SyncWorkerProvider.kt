package uz.uzkassa.smartpos.trade.presentation.app.worker.provider

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import uz.uzkassa.smartpos.core.manager.coroutine.CoroutineContextManager
import uz.uzkassa.smartpos.feature.launcher.data.preference.branch.CurrentBranchPreference
import uz.uzkassa.smartpos.feature.sync.common.data.repository.SyncRepository
import uz.uzkassa.smartpos.feature.sync.worker.SyncWorker
import uz.uzkassa.smartpos.trade.data.network.utils.credential.CurrentCredentialParams
import uz.uzkassa.smartpos.trade.presentation.support.dagger2.worker.WorkerProvider
import javax.inject.Inject

class SyncWorkerProvider @Inject constructor(
    private val coroutineContextManager: CoroutineContextManager,
    private val currentBranchPreference: CurrentBranchPreference,
    private val currentCredentialParams: CurrentCredentialParams,
    private val syncRepository: SyncRepository
) : WorkerProvider {

    override fun provideWorker(
        context: Context,
        workerParams: WorkerParameters
    ): ListenableWorker = SyncWorker(
        branchId = currentBranchPreference.branchId,
        coroutineContextManager = coroutineContextManager,
        context = context,
        isSyncAllowed = currentCredentialParams.isResumed,
        syncRepository = syncRepository,
        workerParams = workerParams
    )
}