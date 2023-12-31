package com.example.todoapp.data.remote.mapper

import com.example.todoapp.data.CurrentDeviceId
import com.example.todoapp.data.local.model.ToDoItemLocal
import com.example.todoapp.data.remote.model.ToDoItemRemote
import com.example.todoapp.domain.model.ToDoItemImportance
import javax.inject.Inject

/**
 * Maps a local representation of a ToDoItem, [ToDoItemLocal], to its remote representation,
 * [ToDoItemRemote], in order to prepare the data for remote API communication.
 * Responsible for converting the data to match the structure and format expected by the remote API.
 */
class ToDoItemRemoteMapper @Inject constructor(
    private val currentDeviceId: CurrentDeviceId,
) {

    fun map(toDoItemLocal: ToDoItemLocal): ToDoItemRemote {
        return ToDoItemRemote(
            id = toDoItemLocal.id,
            text = toDoItemLocal.text,
            importance = ToDoItemImportance.fromValue(toDoItemLocal.importance).toString(),
            deadLineEpochDay = toDoItemLocal.deadLineEpochDay,
            isDone = toDoItemLocal.isDone,
            creationDateMillis = toDoItemLocal.creationDateMillis,
            editDateMillis = toDoItemLocal.editDateMillis,
            lastUpdatedBy = currentDeviceId.id,
        )
    }
}