package com.example.todoapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class ToDoItemRemote(
    @SerializedName("id") var id: String,
    @SerializedName("text") val text: String = "",
    @SerializedName("importance") val importance: String = "basic",
    @SerializedName("deadline") val deadLineDateMillis: Long? = 0,
    @SerializedName("done") val isDone: Boolean,
    @SerializedName("color") val color: String? = "#FFFFFF",
    @SerializedName("created_at") val creationDateMillis: Long,
    @SerializedName("changed_at") val editDateMillis: Long,
    @SerializedName("last_updated_by") val lastUpdatedBy: String? = "model"
)
