package com.example.todoapp.data

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todoapp.ToDoApp
import javax.inject.Inject

class LocalListUpdateWorker @Inject constructor(
     private val context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val todoItemsRepository = (context.applicationContext as ToDoApp).appComponent.toDoItemsRepository()
        val result = todoItemsRepository.getSynchronizationResult()
        return if (result.isSuccess) {
            Log.d("TAGs", "doWork: done")
            Result.success()
        } else {
            Log.d("TAGs", "doWork: error")
            Result.failure()
        }
    }
}