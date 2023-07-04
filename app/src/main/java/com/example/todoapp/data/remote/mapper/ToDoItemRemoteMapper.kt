package com.example.todoapp.data.remote.mapper

import com.example.todoapp.data.CurrentDeviceId
import com.example.todoapp.data.local.model.ToDoItemLocal
import com.example.todoapp.data.remote.model.ToDoItemRemote
import com.example.todoapp.domain.model.ToDoItem
import com.example.todoapp.domain.model.ToDoItemImportance
import javax.inject.Inject

class ToDoItemRemoteMapper @Inject constructor(
    private val currentDeviceId: CurrentDeviceId,
) {

    fun map(toDoItem: ToDoItem): ToDoItemRemote {
        val importance = when (toDoItem.priority) {
            ToDoItemImportance.IMPORTANT -> {
                "important"
            }

            ToDoItemImportance.LOW -> {
                "low"
            }

            ToDoItemImportance.BASIC -> {
                "basic"
            }
        }

        return ToDoItemRemote(
            id = toDoItem.id,
            text = toDoItem.text,
            importance = importance,
            deadLineDateMillis = toDoItem.deadLineDate?.time,
            isDone = toDoItem.isDone,
            creationDateMillis = toDoItem.creationDate.time,
            editDateMillis = toDoItem.editDate.time,
            lastUpdatedBy = currentDeviceId.id,
        )
    }

    fun map(toDoItemLocal: ToDoItemLocal): ToDoItemRemote {
        val importance = when (ToDoItemImportance.from(toDoItemLocal.importance)) {
            ToDoItemImportance.IMPORTANT -> {
                "high"
            }

            ToDoItemImportance.LOW -> {
                "low"
            }

            ToDoItemImportance.BASIC -> {
                "basic"
            }
        }

        return ToDoItemRemote(
            id = toDoItemLocal.id,
            text = toDoItemLocal.text,
            importance = importance,
            deadLineDateMillis = toDoItemLocal.deadLineDateMillis,
            isDone = toDoItemLocal.isDone,
            creationDateMillis = toDoItemLocal.creationDateMillis,
            editDateMillis = toDoItemLocal.editDateMillis,
            lastUpdatedBy = currentDeviceId.id,
        )
    }
}