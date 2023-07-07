package com.example.todoapp.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import com.example.todoapp.di.scope.DataWorkScope
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val UPDATE_PERIOD_HOURS = 8L
const val FLEX_UPDATE_PERIOD_HOURS = 1L

@DataWorkScope
class WorkerSupplier @Inject constructor(
    private val context: Context
) {

    fun enqueueSynchronizeDataWorker() {
        val constraints = Constraints.Builder().setRequiredNetworkType(
            NetworkType.CONNECTED
        ).build()

        val workRequest = PeriodicWorkRequestBuilder<Worker>(
            UPDATE_PERIOD_HOURS,
            TimeUnit.HOURS,
            FLEX_UPDATE_PERIOD_HOURS,
            TimeUnit.HOURS,
        ).setConstraints(constraints).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SynchronizeDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    fun cancelSynchronizeDataWorker() {
        WorkManager.getInstance(context).cancelUniqueWork(SynchronizeDataWorker.WORK_NAME)
    }
}