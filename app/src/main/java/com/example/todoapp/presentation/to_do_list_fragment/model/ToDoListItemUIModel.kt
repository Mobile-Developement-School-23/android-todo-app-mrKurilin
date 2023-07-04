package com.example.todoapp.presentation.to_do_list_fragment.model

import com.example.todoapp.domain.model.ToDoItemImportance

data class ToDoListItemUIModel(
    val id: String,
    val text: String,
    val isDone: Boolean,
    val priority: ToDoItemImportance,
    val deadLineDate: String?,
)