package com.example.todoapp.data.remote.model

import com.example.todoapp.data.remote.okStatus
import com.google.gson.annotations.SerializedName

data class ToDoItemRemoteListResponse(
    @SerializedName("status") val status: String = okStatus,
    @SerializedName("list") val list: List<ToDoItemRemote>,
    @SerializedName("revision") val revision: Int,
)