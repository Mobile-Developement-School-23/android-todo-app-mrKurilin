package com.example.todoapp.presentation.entrytodoitem

import com.example.todoapp.presentation.entrytodoitem.model.ToDoItemUIModel

/**
 * UI states related to creating and editing a ToDoItem in the entry ToDoItem screen.
 */
sealed class ToDoItemEntryUIState {

    object Loading : ToDoItemEntryUIState()

    data class ToDoItemUIModelUpdated(val toDoItemUIModel: ToDoItemUIModel) : ToDoItemEntryUIState()

    object Closing : ToDoItemEntryUIState()
}