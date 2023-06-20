package com.example.todoapp.presentation.to_do_item_entry.model

data class ToDoItemUIModel(
    val text: String,
    val priorityValue: Int,
    val deadLineDate: Long?,
)