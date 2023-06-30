package com.example.todoapp.presentation.entry_to_do_item_fragment.model

data class ToDoItemUIModel(
    val text: String,
    val priorityValue: Int,
    val deadLineDate: Long?,
)