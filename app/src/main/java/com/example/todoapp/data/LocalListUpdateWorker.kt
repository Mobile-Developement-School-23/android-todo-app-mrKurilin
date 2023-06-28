package com.example.todoapp.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todoapp.ToDoApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalListUpdateWorker @Inject constructor(
    private val context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val todoItemsRepository = (context.applicationContext as ToDoApp).todoItemsRepository
            todoItemsRepository.updateData()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}