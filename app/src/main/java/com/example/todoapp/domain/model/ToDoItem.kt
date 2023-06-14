package com.example.todoapp.domain.model

import java.util.Date

data class ToDoItem(
    val id: String,
    val text: String,
    val priority: ToDoItemPriority,
    val creationDate: Date,
    val isDone: Boolean,
    val deadLineDate: Date? = null,
    val editDate: Date? = null,
)