package com.example.todoapp.presentation.entrytodoitem.model

data class ToDoItemUIModel(
    val text: String,
    val priorityValue: Int,
    val deadLineDate: Long?,
)