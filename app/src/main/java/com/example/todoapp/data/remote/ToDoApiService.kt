package com.example.todoapp.data.remote

import com.example.todoapp.data.remote.model.EntryToDoItemRemoteRequest
import com.example.todoapp.data.remote.model.ToDoItemRemoteListResponse
import com.example.todoapp.data.remote.model.ToDoItemRemoteResponse
import com.example.todoapp.data.remote.model.UpdateToDoItemRemoteListRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ToDoApiService {

    @GET("list")
    suspend fun getToDoItemListRemote(): ToDoItemRemoteListResponse

    @PATCH("list")
    suspend fun updateToDoItemListRemote(
        @Header(LAST_KNOWN_REVISION_KEY) revision: Int,
        @Body updateToDoItemRemoteListRequest: UpdateToDoItemRemoteListRequest
    ): ToDoItemRemoteListResponse

    @POST("list")
    suspend fun addToDoItemRemote(
        @Header(LAST_KNOWN_REVISION_KEY) revision: Int,
        @Body entryToDoItemRemoteRequest: EntryToDoItemRemoteRequest,
    ): ToDoItemRemoteResponse

    @PUT("list/{id}")
    suspend fun updateToDoItemRemote(
        @Header(LAST_KNOWN_REVISION_KEY) revision: Int,
        @Path("id") id: String,
        @Body entryToDoItemRemoteRequest: EntryToDoItemRemoteRequest,
    ): ToDoItemRemoteResponse

    @DELETE("list/{id}")
    suspend fun deleteToDoItemRemote(
        @Header(LAST_KNOWN_REVISION_KEY) revision: Int,
        @Path("id") id: String,
    ): ToDoItemRemoteResponse
}