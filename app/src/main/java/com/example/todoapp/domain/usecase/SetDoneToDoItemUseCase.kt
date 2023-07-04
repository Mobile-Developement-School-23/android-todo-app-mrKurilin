package com.example.todoapp.domain.usecase

import com.example.todoapp.data.ToDoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetDoneToDoItemUseCase @Inject constructor(
    private val toDoItemsRepository: ToDoItemsRepository
) {

    suspend fun set(id: String) = withContext(Dispatchers.IO) {
        toDoItemsRepository.setDoneToDoItem(id)
    }
}