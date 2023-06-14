package com.example.todoapp.presentation.to_do_item_entry.model

import androidx.annotation.StringRes

data class ToDoItemUIModel(
    val text: String,
    @StringRes
    val priorityStringId: Int,
    val deadLineDate: Long?,
)