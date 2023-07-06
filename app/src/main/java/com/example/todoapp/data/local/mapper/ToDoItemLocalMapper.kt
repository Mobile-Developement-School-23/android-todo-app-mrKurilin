package com.example.todoapp.data.local.mapper

import com.example.todoapp.data.local.model.ToDoItemLocalRemoteAction
import com.example.todoapp.data.local.model.ToDoItemLocal
import com.example.todoapp.data.remote.model.ToDoItemRemote
import com.example.todoapp.domain.model.ToDoItem
import com.example.todoapp.domain.model.ToDoItemImportance
import java.util.Date
import javax.inject.Inject

/**
 * Mapper class to convert between different representations of [ToDoItemLocal]
 */
class ToDoItemLocalMapper @Inject constructor() {

    fun map(toDoItemLocal: ToDoItemLocal): ToDoItem {
        val deadLineDate = if (toDoItemLocal.deadLineDateMillis == null) {
            null
        } else {
            Date(toDoItemLocal.deadLineDateMillis)
        }

        return ToDoItem(
            id = toDoItemLocal.id,
            text = toDoItemLocal.text,
            isDone = toDoItemLocal.isDone,
            creationDate = Date(toDoItemLocal.creationDateMillis),
            editDate = Date(toDoItemLocal.editDateMillis),
            priority = ToDoItemImportance.from(toDoItemLocal.importance),
            deadLineDate = deadLineDate,
        )
    }

    fun map(toDoItem: ToDoItem): ToDoItemLocal {
        return ToDoItemLocal(
            id = toDoItem.id,
            text = toDoItem.text,
            isDone = toDoItem.isDone,
            creationDateMillis = toDoItem.creationDate.time,
            editDateMillis = toDoItem.editDate.time,
            importance = toDoItem.priority.value,
            deadLineDateMillis = toDoItem.deadLineDate?.time,
            toDoItemLocalRemoteAction = ToDoItemLocalRemoteAction.ADD
        )
    }

    fun map(toDoItemRemote: ToDoItemRemote): ToDoItemLocal {
        val importance = ToDoItemImportance.fromString(toDoItemRemote.importance)
        return ToDoItemLocal(
            id = toDoItemRemote.id,
            text = toDoItemRemote.text,
            isDone = toDoItemRemote.isDone,
            creationDateMillis = toDoItemRemote.creationDateMillis,
            editDateMillis = toDoItemRemote.editDateMillis,
            importance = importance.value,
            deadLineDateMillis = toDoItemRemote.deadLineDateMillis,
        )
    }
}