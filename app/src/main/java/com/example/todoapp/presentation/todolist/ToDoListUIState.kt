package com.example.todoapp.presentation.todolist

import com.example.todoapp.presentation.Notification
import com.example.todoapp.presentation.todolist.model.ToDoListItemUIModel

/**
 * Represents the UI state of the ToDoList feature in the application.
 */
data class ToDoListUIState(
    val isDoneItemsVisible: Boolean = true,
    val isUpdatingData: Boolean = false,
    val isNoInternetConnection: Boolean = false,
    val isAuthorized: Boolean = false,
    val doneToDoItemsCount: Int = 0,
    val notification: Notification? = null,
    val toDoListItemUIModelList: List<ToDoListItemUIModel> = listOf(),
)