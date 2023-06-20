package com.example.todoapp.data.local

import com.example.todoapp.data.local.model.ToDoItemLocal
import com.example.todoapp.domain.model.ToDoItem
import com.example.todoapp.domain.model.ToDoItemPriority
import java.util.Date

class ToDoItemLocalMapper {

    fun toToDoItem(toDoItemLocal: ToDoItemLocal): ToDoItem {
        val creationDate = Date(toDoItemLocal.creationDateMillis)

        val editDate = if (toDoItemLocal.editDateMillis != null) {
            Date(toDoItemLocal.editDateMillis)
        } else {
            null
        }

        val priority = ToDoItemPriority.from(toDoItemLocal.priorityInt)

        val deadLineDate = if (toDoItemLocal.deadLineDateMillis == null) {
            null
        } else {
            Date(toDoItemLocal.deadLineDateMillis)
        }

        return ToDoItem(
            id = toDoItemLocal.id,
            text = toDoItemLocal.text,
            isDone = toDoItemLocal.isDone,
            creationDate = creationDate,
            editDate = editDate,
            priority = priority,
            deadLineDate = deadLineDate,
        )
    }

    fun toToDoItemLocal(toDoItem: ToDoItem): ToDoItemLocal {
        return ToDoItemLocal(
            id = toDoItem.id,
            text = toDoItem.text,
            isDone = toDoItem.isDone,
            creationDateMillis = toDoItem.creationDate.time,
            editDateMillis = toDoItem.editDate?.time,
            priorityInt = toDoItem.priority.value,
            deadLineDateMillis = toDoItem.deadLineDate?.time,
        )
    }
}