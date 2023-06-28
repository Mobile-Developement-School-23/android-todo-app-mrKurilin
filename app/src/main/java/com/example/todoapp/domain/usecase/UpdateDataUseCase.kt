package com.example.todoapp.domain.usecase

import com.example.todoapp.data.ToDoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateDataUseCase @Inject constructor(
    private val toDoItemsRepository: ToDoItemsRepository
) {

    suspend fun update() = withContext(Dispatchers.IO) {
        toDoItemsRepository.updateData()
    }
}