package com.example.todoapp.presentation.to_do_item_entry.model

import com.example.todoapp.R
import com.example.todoapp.domain.model.ToDoItem
import com.example.todoapp.domain.model.ToDoItemPriority
import java.util.Date

class ToDoItemUIMapper {

    fun toToDoItemUIModel(todoItem: ToDoItem): ToDoItemUIModel {
        val priorityStringId = when (todoItem.priority) {
            ToDoItemPriority.LOW -> {
                R.string.low
            }

            ToDoItemPriority.NORMAL -> {
                R.string.no
            }

            ToDoItemPriority.HIGH -> {
                R.string.high
            }
        }

        return ToDoItemUIModel(
            text = todoItem.text,
            priorityStringId = priorityStringId,
            deadLineDate = todoItem.deadLineDate?.time
        )
    }

    fun toToDoItem(
        toDoItemUIModel: ToDoItemUIModel,
    ): ToDoItem {
        val priority = when (toDoItemUIModel.priorityStringId) {
            R.string.no -> {
                ToDoItemPriority.NORMAL
            }

            R.string.high -> {
                ToDoItemPriority.HIGH
            }

            R.string.low -> {
                ToDoItemPriority.LOW
            }

            else -> {
                throw IllegalStateException()
            }
        }
        val creationDate = Date()
        val deadLineDate = if (toDoItemUIModel.deadLineDate == null) {
            null
        } else {
            Date(toDoItemUIModel.deadLineDate)
        }
        return ToDoItem(
            id = creationDate.time.toString(),
            text = toDoItemUIModel.text,
            priority = priority,
            creationDate = creationDate,
            isDone = false,
            deadLineDate = deadLineDate,
            editDate = null,
        )
    }
}