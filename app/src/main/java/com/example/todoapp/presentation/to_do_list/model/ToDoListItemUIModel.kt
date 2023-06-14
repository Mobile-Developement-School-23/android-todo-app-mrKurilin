package com.example.todoapp.presentation.to_do_list.model

import com.example.todoapp.domain.model.ToDoItemPriority

data class ToDoListItemUIModel(
    val id: String,
    val text: String,
    val isDone: Boolean,
    val priority: ToDoItemPriority,
    val deadLineDate: String?,
)