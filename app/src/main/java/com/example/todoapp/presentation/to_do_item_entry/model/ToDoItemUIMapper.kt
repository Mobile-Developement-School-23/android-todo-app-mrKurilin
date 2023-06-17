package com.example.todoapp.presentation.to_do_item_entry.model

import com.example.todoapp.domain.model.ToDoItem
import com.example.todoapp.domain.model.ToDoItemPriority
import java.util.Date

class ToDoItemUIMapper {

    fun toToDoItemUIModel(todoItem: ToDoItem): ToDoItemUIModel {
        return ToDoItemUIModel(
            text = todoItem.text,
            priorityValue = todoItem.priority.value,
            deadLineDate = todoItem.deadLineDate?.time
        )
    }

    fun toToDoItem(
        toDoItemUIModel: ToDoItemUIModel,
    ): ToDoItem {
        val creationDate = Date()
        val deadLineDate = if (toDoItemUIModel.deadLineDate == null) {
            null
        } else {
            Date(toDoItemUIModel.deadLineDate)
        }
        return ToDoItem(
            id = creationDate.time.toString(),
            text = toDoItemUIModel.text,
            priority = ToDoItemPriority.from(toDoItemUIModel.priorityValue),
            creationDate = creationDate,
            isDone = false,
            deadLineDate = deadLineDate,
            editDate = null,
        )
    }
}