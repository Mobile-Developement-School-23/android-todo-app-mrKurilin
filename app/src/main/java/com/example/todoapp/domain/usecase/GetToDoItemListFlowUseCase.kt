package com.example.todoapp.domain.usecase

import com.example.todoapp.data.ToDoItemsRepository
import com.example.todoapp.domain.model.ToDoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetToDoItemListFlowUseCase @Inject constructor(
    private val repository: ToDoItemsRepository,
) {

    suspend fun get(): Flow<List<ToDoItem>> = withContext(Dispatchers.IO) {
        return@withContext repository.getToDoItemListFlow()
    }
}