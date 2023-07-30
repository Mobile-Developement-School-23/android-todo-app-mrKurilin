package com.example.todoapp.domain.usecase

import com.example.todoapp.data.ToDoItemsRepository
import com.example.todoapp.domain.model.ToDoItemImportance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateToDoItemUseCase @Inject constructor(
    private val todoItemsRepository: ToDoItemsRepository,
) {

    suspend fun update(
        toDoItemId: String,
        text: String,
        deadLineDateMillis: Long?,
        importance: ToDoItemImportance
    ) = withContext(Dispatchers.IO) {
        todoItemsRepository.updateToDoItem(
            toDoItemId = toDoItemId,
            text = text,
            deadLineDateMillis = deadLineDateMillis,
            priority = importance,
        )
    }
}