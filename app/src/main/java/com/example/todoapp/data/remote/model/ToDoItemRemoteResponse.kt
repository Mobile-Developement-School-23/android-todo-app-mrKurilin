package com.example.todoapp.data.remote.model

import com.example.todoapp.data.remote.okStatus
import com.google.gson.annotations.SerializedName

data class ToDoItemRemoteResponse(
    @SerializedName("status") val status: String = okStatus,
    @SerializedName("element") val toDoItemRemote: ToDoItemRemote,
    @SerializedName("revision") val revision: Int,
)