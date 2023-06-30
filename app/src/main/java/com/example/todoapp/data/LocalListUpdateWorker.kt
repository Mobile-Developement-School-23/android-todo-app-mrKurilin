package com.example.todoapp.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todoapp.ToDoApp
import javax.inject.Inject

class LocalListUpdateWorker @Inject constructor(
    private val context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val todoItemsRepository = (context.applicationContext as ToDoApp).todoItemsRepository
        val result = todoItemsRepository.getSynchronizationResult()
        return if (result.isSuccess) {
            Result.success()
        } else {
            Result.failure()
        }
    }
}