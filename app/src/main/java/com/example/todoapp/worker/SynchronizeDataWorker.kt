package com.example.todoapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todoapp.ToDoApp
import javax.inject.Inject

/**
 * Performs the synchronization of local ToDoItems data with the remote data source.
 */
class SynchronizeDataWorker @Inject constructor(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = try {
        val toDoApp = applicationContext as ToDoApp
        val component = toDoApp.provideAppComponent().dataWorkComponent()
        val todoItemsRepository = component.toDoItemsRepository()
        todoItemsRepository.synchronizeData()
        Result.success()
    } catch (exception: Exception) {
        Result.failure()
    }

    companion object {
        const val WORK_NAME = "SynchronizeDataWorker"
    }
}