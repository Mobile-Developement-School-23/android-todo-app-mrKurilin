package com.example.todoapp.presentation.entry_to_do_item_fragment

import com.example.todoapp.presentation.entry_to_do_item_fragment.model.ToDoItemUIModel

sealed class ToDoItemEntryUIState {

    object Loading : ToDoItemEntryUIState()

    data class ToDoItemUIModelUpdated(val toDoItemUIModel: ToDoItemUIModel) : ToDoItemEntryUIState()

    object CanBeClosed : ToDoItemEntryUIState()
}