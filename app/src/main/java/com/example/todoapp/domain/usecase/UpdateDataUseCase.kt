package com.example.todoapp.domain.usecase

import com.example.todoapp.data.ToDoItemsRepository
import javax.inject.Inject

class UpdateDataUseCase @Inject constructor(
    private val toDoItemsRepository: ToDoItemsRepository
) {

    suspend fun update(): Result<Any?> = try {
        toDoItemsRepository.synchronizeData()
        Result.success(null)
    } catch (exception: Exception) {
        Result.failure(exception)
    }
}