package com.example.todoapp.data.local.model

data class ToDoItemLocal(
    val id: String,
    val text: String,
    val isDone: Boolean,
    val creationDateMillis: Long,
    val editDateMillis: Long?,
    val priorityInt: Int,
    val deadLineDateMillis: Long?,
)