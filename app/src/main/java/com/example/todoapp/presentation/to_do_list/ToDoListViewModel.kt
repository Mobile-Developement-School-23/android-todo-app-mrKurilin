package com.example.todoapp.presentation.to_do_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.todoapp.ToDoApp
import com.example.todoapp.presentation.to_do_list.model.ToDoListItemUIMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class ToDoListViewModel(app: Application) : AndroidViewModel(app) {

    private val toDoApp = app as ToDoApp

    private val toDoListItemUIMapper = ToDoListItemUIMapper()

    private val todoItemsRepository = toDoApp.provideTodoItemsRepository()

    val toDoListStateFlow = todoItemsRepository.getToDoItemListFlow().map { list ->
        list.map { toDoItem ->
            toDoListItemUIMapper.map(toDoItem)
        }
    }

    private val _isDoneItemsVisibleStateFlow = MutableStateFlow(true)
    val isDoneItemsVisibleStateFlow: StateFlow<Boolean> = _isDoneItemsVisibleStateFlow

    fun deleteToDoItem(toDoItemId: String) {
        todoItemsRepository.deleteToDoItem(toDoItemId)
    }

    fun setDoneToDoItem(toDoItemId: String) {
        todoItemsRepository.setDoneToDoItem(toDoItemId)
    }

    fun changeDoneItemsVisibility() {
        _isDoneItemsVisibleStateFlow.value = !_isDoneItemsVisibleStateFlow.value
    }
}