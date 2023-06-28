package com.example.todoapp.domain.usecase

import com.example.todoapp.data.ToDoItemsRepository
import com.example.todoapp.domain.model.ToDoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddToDoItemUseCase @Inject constructor(
    private val todoItemsRepository: ToDoItemsRepository,
) {

    suspend fun add(toDoItem: ToDoItem) = withContext(Dispatchers.IO) {
        todoItemsRepository.addToDoItem(toDoItem)
    }
}