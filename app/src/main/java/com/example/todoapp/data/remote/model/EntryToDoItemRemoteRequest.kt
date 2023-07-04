package com.example.todoapp.data.remote.model

import com.example.todoapp.data.remote.OK_STATUS
import com.google.gson.annotations.SerializedName

data class EntryToDoItemRemoteRequest(
    @SerializedName("status") val status: String = OK_STATUS,
    @SerializedName("element") val toDoItemRemote: ToDoItemRemote,
)