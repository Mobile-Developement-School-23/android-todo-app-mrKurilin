package com.example.todoapp.domain.usecase

import com.example.todoapp.data.ToDoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteToDoItemByIdUseCase @Inject constructor(
    private val toDoItemsRepository: ToDoItemsRepository,
) {

    suspend fun delete(id: String) = withContext(Dispatchers.IO) {
        toDoItemsRepository.deleteToDoItemById(id)
    }
}