package com.example.todoapp.data.remote

import android.content.SharedPreferences
import com.example.todoapp.data.remote.model.EntryToDoItemRemoteRequest
import com.example.todoapp.data.remote.model.ToDoItemRemoteListResponse
import com.example.todoapp.data.remote.model.ToDoItemRemoteResponse
import javax.inject.Inject

class ToDoItemsRemoteDataSource @Inject constructor(
    private val toDoApiService: ToDoApiService,
    private val sharedPreferences: SharedPreferences,
) {

    suspend fun getToDoItemListRemote(): ToDoItemRemoteListResponse {
        return toDoApiService.getToDoItemListRemote()
    }

    suspend fun addToDoItemRemote(
        addToDoItemRemoteRequest: EntryToDoItemRemoteRequest,
    ): ToDoItemRemoteResponse {
        val lastKnownRevision = sharedPreferences.getInt(lastKnownRevision, 0)

        return toDoApiService.addToDoItemRemote(
            entryToDoItemRemoteRequest = addToDoItemRemoteRequest,
            revision = lastKnownRevision
        )
    }

    suspend fun updateToDoItemRemote(
        updateToDoItemRemoteRequest: EntryToDoItemRemoteRequest,
    ): ToDoItemRemoteResponse {
        return toDoApiService.updateToDoItemRemote(
            id = updateToDoItemRemoteRequest.toDoItemRemote.id,
            entryToDoItemRemoteRequest = updateToDoItemRemoteRequest
        )
    }

    suspend fun deleteToDoItemRemoteById(
        toDoItemId: String,
    ): ToDoItemRemoteResponse {
        val lastKnownRevision = sharedPreferences.getInt(lastKnownRevision, 0)
        return toDoApiService.deleteToDoItemRemote(
            id = toDoItemId,
            revision = lastKnownRevision
        )
    }
}