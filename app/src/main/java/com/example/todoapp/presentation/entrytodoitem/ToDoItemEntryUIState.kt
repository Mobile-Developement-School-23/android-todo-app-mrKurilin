package com.example.todoapp.presentation.entrytodoitem

import com.example.todoapp.domain.model.ToDoItemImportance
import com.example.todoapp.presentation.entrytodoitem.model.ToDoItemUIModel

/**
 * UI states related to creating and editing a ToDoItem in the entry ToDoItem screen.
 */
sealed class ToDoItemEntryUIState(
    val toDoItemUIModel: ToDoItemUIModel = ToDoItemUIModel(
        text = "",
        priorityValue = ToDoItemImportance.BASIC.value,
        deadLineDate = null,
    )
) {

    object Loading : ToDoItemEntryUIState()

    object ShowInit : ToDoItemEntryUIState()

    class ToDoItemUIModelUpdated(
        toDoItemUIModel: ToDoItemUIModel
    ) : ToDoItemEntryUIState(toDoItemUIModel)

    object Closing : ToDoItemEntryUIState()
}