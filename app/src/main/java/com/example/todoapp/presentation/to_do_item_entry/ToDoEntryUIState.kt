package com.example.todoapp.presentation.to_do_item_entry

import com.example.todoapp.presentation.to_do_item_entry.model.ToDoItemUIModel

data class ToDoEntryUIState(
    val ableToSave: Boolean = false,
    val enabledDeadLine: Boolean = false,
    val toDoItemUIModel: ToDoItemUIModel? = null,
)