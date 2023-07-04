package com.example.todoapp.presentation.to_do_list_fragment

import com.example.todoapp.presentation.Notification
import com.example.todoapp.presentation.to_do_list_fragment.model.ToDoListItemUIModel

data class ToDoListUIState(
    val isDoneItemsVisible: Boolean = true,
    val isUpdatingData: Boolean = false,
    val isNoInternetConnection: Boolean = false,
    val isAuthorized: Boolean = false,
    val doneToDoItemsCount: Int = 0,
    val notification: Notification? = null,
    val toDoListItemUIModelList: List<ToDoListItemUIModel> = listOf(),
)