package com.example.todoapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class EntryToDoItemRemoteRequest(
    @SerializedName("element") val toDoItemRemote: ToDoItemRemote,
    @SerializedName("status") val status: String = "OK",
)