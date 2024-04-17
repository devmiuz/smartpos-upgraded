package uz.uzkassa.smartpos.trade.presentation.support.dagger2.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

interface WorkerProvider {

    fun provideWorker(context: Context, workerParams: WorkerParameters): ListenableWorker
}