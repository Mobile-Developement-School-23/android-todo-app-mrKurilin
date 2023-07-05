package com.example.todoapp.presentation.entrytodoitem

import com.example.todoapp.presentation.entrytodoitem.model.ToDoItemUIModel

sealed class ToDoItemEntryUIState {

    object Loading : ToDoItemEntryUIState()

    data class ToDoItemUIModelUpdated(val toDoItemUIModel: ToDoItemUIModel) : ToDoItemEntryUIState()

    object CanBeClosed : ToDoItemEntryUIState()
}