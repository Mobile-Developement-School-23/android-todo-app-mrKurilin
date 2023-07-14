package com.example.todoapp.presentation.entrytodoitem

import com.example.todoapp.presentation.entrytodoitem.model.ToDoItemUIModel

/**
 * UI states related to creating and editing a ToDoItem in the entry ToDoItem screen.
 */
data class ToDoItemEntryUIState(
    val isLoading: Boolean = false,
    val toDoItemUIModel: ToDoItemUIModel = ToDoItemUIModel(),
    val canBeClosed: Boolean = false
)