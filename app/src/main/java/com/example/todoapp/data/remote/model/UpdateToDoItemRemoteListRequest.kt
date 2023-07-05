package com.example.todoapp.data.remote.model

import com.example.todoapp.data.remote.OK_STATUS
import com.google.gson.annotations.SerializedName

/**
 * Remote API request payload for updating remote ToDoItem list.
 */
data class UpdateToDoItemRemoteListRequest(
    @SerializedName("list") val list: List<ToDoItemRemote>,
    @SerializedName("status") val status: String = OK_STATUS,
)