package com.example.todoapp.presentation.entrytodoitem.model

import com.example.todoapp.R
import com.example.todoapp.domain.model.ToDoItem
import com.example.todoapp.domain.model.ToDoItemImportance
import java.util.Date
import javax.inject.Inject

/**
 * Mapping between [ToDoItem] and [ToDoItemUIModel].
 */
class ToDoItemUIMapper @Inject constructor() {

    fun map(todoItem: ToDoItem): ToDoItemUIModel {
        val priorityStringId = when (todoItem.priority) {
            ToDoItemImportance.LOW -> {
                R.string.low
            }

            ToDoItemImportance.BASIC -> {
                R.string.basic
            }

            ToDoItemImportance.IMPORTANT -> {
                R.string.high_importance
            }
        }
        return ToDoItemUIModel(
            id = todoItem.id,
            text = todoItem.text,
            priorityStringId = priorityStringId,
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

        val priority = when (toDoItemUIModel.priorityStringId) {
            R.string.low -> {
                ToDoItemImportance.LOW
            }

            R.string.basic -> {
                ToDoItemImportance.BASIC
            }

            R.string.high_importance -> {
                ToDoItemImportance.IMPORTANT
            }

            else -> {
                error("Illegal string id")
            }
        }
        return ToDoItem(
            id = creationDate.time.toString(),
            text = toDoItemUIModel.text,
            priority = priority,
            creationDate = creationDate,
            isDone = false,
            deadLineDate = deadLineDate,
            editDate = creationDate,
        )
    }
}