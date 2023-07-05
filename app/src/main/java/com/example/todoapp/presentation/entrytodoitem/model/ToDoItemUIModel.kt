package com.example.todoapp.presentation.entrytodoitem.model

/**
 *  Represents the UI model for a ToDoItem used in the entry screen.
 */
data class ToDoItemUIModel(
    val text: String,
    val priorityValue: Int,
    val deadLineDate: Long?,
)