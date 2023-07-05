package com.example.todoapp.data.remote.model

import com.example.todoapp.data.remote.OK_STATUS
import com.google.gson.annotations.SerializedName

/**
 * Remote API request payload for adding or updating a ToDoItem.
 */
data class EntryToDoItemRemoteRequest(
    @SerializedName("status") val status: String = OK_STATUS,
    @SerializedName("element") val toDoItemRemote: ToDoItemRemote,
)