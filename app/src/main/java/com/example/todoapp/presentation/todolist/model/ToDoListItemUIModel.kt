package com.example.todoapp.presentation.todolist.model

import com.example.todoapp.domain.model.ToDoItemImportance

/**
 *  Represents a UI-specific model for displaying a ToDoItem in the ToDoList feature.
 */
data class ToDoListItemUIModel(
    val id: String,
    val text: String,
    val isDone: Boolean,
    val importance: ToDoItemImportance,
    val deadLineDate: String?,
)