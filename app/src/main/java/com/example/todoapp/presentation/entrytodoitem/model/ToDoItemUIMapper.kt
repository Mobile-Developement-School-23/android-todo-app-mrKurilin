package com.example.todoapp.presentation.entrytodoitem.model

import com.example.todoapp.domain.model.ToDoItem
import com.example.todoapp.domain.model.ToDoItemImportance
import java.util.Date
import javax.inject.Inject

/**
 * Mapping between [ToDoItem] and [ToDoItemUIModel].
 */
class ToDoItemUIMapper @Inject constructor() {

    fun map(todoItem: ToDoItem): ToDoItemUIModel {
        return ToDoItemUIModel(
            text = todoItem.text,
            priorityValue = todoItem.priority.value,
            deadLineDate = todoItem.deadLineDate?.time
        )
    }

    fun map(toDoItemUIModel: ToDoItemUIModel): ToDoItem {
        val creationDate = Date()
        val deadLineDate = if (toDoItemUIModel.deadLineDate == null) {
            null
        } else {
            Date(toDoItemUIModel.deadLineDate)
        }
        return ToDoItem(
            id = creationDate.time.toString(),
            text = toDoItemUIModel.text,
            priority = ToDoItemImportance.from(toDoItemUIModel.priorityValue),
            creationDate = creationDate,
            isDone = false,
            deadLineDate = deadLineDate,
            editDate = creationDate,
        )
    }
}