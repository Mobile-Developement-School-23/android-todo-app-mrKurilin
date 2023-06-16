package com.example.todoapp.data

import com.example.todoapp.data.local.ToDoItemLocalMapper
import com.example.todoapp.data.local.ToDoItemsLocalDataSource
import com.example.todoapp.domain.model.ToDoItem
import com.example.todoapp.domain.model.ToDoItemPriority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoItemsRepository(
    private val toDoItemsLocalDataSource: ToDoItemsLocalDataSource,
    private val toDoItemLocalMapper: ToDoItemLocalMapper,
) {

    fun getToDoItemListFlow(): Flow<List<ToDoItem>> {
        return toDoItemsLocalDataSource.getToDoItemListStateFlow().map { list ->
            list.map { toDoItemLocal ->
                toDoItemLocalMapper.toToDoItem(toDoItemLocal)
            }
        }
    }

    fun addToDoItem(todoItem: ToDoItem) {
        val toDoItemLocal = toDoItemLocalMapper.toToDoItemLocal(todoItem)
        toDoItemsLocalDataSource.addToDoItem(toDoItemLocal)
    }

    fun deleteToDoItem(toDoItemId: String) {
        toDoItemsLocalDataSource.deleteToDoItemById(toDoItemId)
    }

    fun setDoneToDoItem(toDoItemId: String) {
        toDoItemsLocalDataSource.setDoneToDoItem(toDoItemId)
    }

    fun getToDoItem(toDoItemId: String): ToDoItem {
        val toDoItemLocal = toDoItemsLocalDataSource.getToDoItemLocal(toDoItemId)
        return toDoItemLocalMapper.toToDoItem(toDoItemLocal)
    }

    fun updateToDoItem(
        toDoItemId: String,
        text: String,
        deadLineDate: Long?,
        priority: ToDoItemPriority
    ) {
        val toDoItemLocal = toDoItemsLocalDataSource.getToDoItemLocal(toDoItemId).copy(
            text = text,
            deadLineDateMillis = deadLineDate,
            priorityInt = priority.value
        )
        toDoItemsLocalDataSource.updateToDoItemLocal(toDoItemLocal)
    }
}
