package com.example.todoapp.data.remote

import android.content.SharedPreferences
import com.example.todoapp.data.local.model.ToDoItemAction
import com.example.todoapp.data.remote.model.EntryToDoItemRemoteRequest
import com.example.todoapp.data.remote.model.ToDoItemRemote
import com.example.todoapp.data.remote.model.ToDoItemRemoteListResponse
import com.example.todoapp.data.remote.model.ToDoItemRemoteResponse
import com.example.todoapp.data.remote.model.UpdateToDoItemRemoteListRequest
import javax.inject.Inject

class ToDoItemsRemoteDataSource @Inject constructor(
    private val toDoApiService: ToDoApiService,
    private val sharedPreferences: SharedPreferences,
) {

    suspend fun getToDoItemListRemote(): ToDoItemRemoteListResponse {
        return toDoApiService.getToDoItemListRemote()
    }

    suspend fun clearRemoteList() {
        toDoApiService.updateToDoItemListRemote(
            sharedPreferences.getInt(LAST_KNOWN_REVISION_KEY, 0),
            UpdateToDoItemRemoteListRequest(listOf())
        )
    }

    suspend fun getInundationResponse(
        toDoItemRemote: ToDoItemRemote,
        toDoItemAction: ToDoItemAction
    ): ToDoItemRemoteResponse {
        return when (toDoItemAction) {
            ToDoItemAction.UPDATE -> {
                updateToDoItemRemote(
                    EntryToDoItemRemoteRequest(toDoItemRemote = toDoItemRemote)
                )
            }

            ToDoItemAction.DELETE -> {
                deleteToDoItemRemoteById(
                    toDoItemRemote.id
                )
            }

            ToDoItemAction.ADD -> {
                addToDoItemRemote(
                    EntryToDoItemRemoteRequest(toDoItemRemote = toDoItemRemote)
                )
            }
        }
    }

    private suspend fun addToDoItemRemote(
        addToDoItemRemoteRequest: EntryToDoItemRemoteRequest,
    ): ToDoItemRemoteResponse {
        val lastKnownRevision = sharedPreferences.getInt(LAST_KNOWN_REVISION_KEY, 0)

        return toDoApiService.addToDoItemRemote(
            entryToDoItemRemoteRequest = addToDoItemRemoteRequest,
            revision = lastKnownRevision
        )
    }

    private suspend fun deleteToDoItemRemoteById(
        toDoItemId: String,
    ): ToDoItemRemoteResponse {
        val lastKnownRevision = sharedPreferences.getInt(LAST_KNOWN_REVISION_KEY, 0)
        return toDoApiService.deleteToDoItemRemote(
            id = toDoItemId,
            revision = lastKnownRevision
        )
    }

    private suspend fun updateToDoItemRemote(
        updateToDoItemRemoteRequest: EntryToDoItemRemoteRequest,
    ): ToDoItemRemoteResponse {
        val lastKnownRevision = sharedPreferences.getInt(LAST_KNOWN_REVISION_KEY, 0)
        return toDoApiService.updateToDoItemRemote(
            revision = lastKnownRevision,
            id = updateToDoItemRemoteRequest.toDoItemRemote.id,
            entryToDoItemRemoteRequest = updateToDoItemRemoteRequest
        )
    }
}