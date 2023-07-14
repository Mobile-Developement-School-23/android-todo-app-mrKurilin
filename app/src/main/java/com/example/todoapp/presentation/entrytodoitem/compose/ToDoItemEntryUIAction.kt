package com.example.todoapp.presentation.entrytodoitem.compose

import androidx.annotation.StringRes

sealed class ToDoItemEntryUIAction {

    object SaveButtonPressed : ToDoItemEntryUIAction()
    object CloseButtonPressed : ToDoItemEntryUIAction()
    object DeleteButtonPressed : ToDoItemEntryUIAction()
    object DeadLineSwitchStateChanged : ToDoItemEntryUIAction()
    data class SelectedDateChanged(val deadLineDate: Long) : ToDoItemEntryUIAction()
    data class TextChanged(val text: String) : ToDoItemEntryUIAction()
    data class PrioritySelected(@StringRes val priorityStringId: Int) : ToDoItemEntryUIAction()
}