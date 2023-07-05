package com.example.todoapp.data

import android.content.SharedPreferences
import com.example.todoapp.data.local.ToDoItemsLocalDataSource
import com.example.todoapp.data.local.mapper.ToDoItemLocalMapper
import com.example.todoapp.data.local.model.ToDoItemAction
import com.example.todoapp.data.local.model.ToDoItemLocal
import com.example.todoapp.data.remote.LAST_KNOWN_REVISION_KEY
import com.example.todoapp.data.remote.ToDoItemsRemoteDataSource
import com.example.todoapp.data.remote.mapper.ToDoItemRemoteMapper
import com.example.todoapp.data.remote.model.ToDoItemRemoteListResponse
import com.example.todoapp.domain.model.ToDoItem
import com.example.todoapp.domain.model.ToDoItemImportance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.util.Date
import javax.inject.Inject

class ToDoItemsRepository @Inject constructor(
    private val toDoItemsLocalDataSource: ToDoItemsLocalDataSource,
    private val toDoItemsRemoteDataSource: ToDoItemsRemoteDataSource,
    private val toDoItemLocalMapper: ToDoItemLocalMapper,
    private val toDoItemRemoteMapper: ToDoItemRemoteMapper,
    private val sharedPreferences: SharedPreferences,
) {

    fun getToDoItemListFlow(): Flow<List<ToDoItem>> {
        return toDoItemsLocalDataSource.getToDoItemListFlow().map { list ->
            list.map { toDoItemLocal ->
                toDoItemLocalMapper.toToDoItem(toDoItemLocal)
            }
        }
    }

    suspend fun addToDoItem(toDoItem: ToDoItem) {
        val toDoItemLocal = toDoItemLocalMapper.map(
            toDoItem = toDoItem,
            toDoItemAction = ToDoItemAction.ADD
        )
        toDoItemsLocalDataSource.addToDoItem(toDoItemLocal)
    }

    suspend fun deleteToDoItemById(toDoItemId: String) {
        val toDoItemLocal = toDoItemsLocalDataSource.getToDoItemLocal(toDoItemId).copy(
            toDoItemAction = ToDoItemAction.DELETE
        )
        toDoItemsLocalDataSource.updateToDoItemLocal(toDoItemLocal)
    }

    suspend fun setDoneToDoItem(toDoItemId: String) {
        val toDoItemLocal = toDoItemsLocalDataSource.getToDoItemLocal(toDoItemId)
        toDoItemsLocalDataSource.updateToDoItemLocal(
            toDoItemLocal.copy(
                isDone = !toDoItemLocal.isDone,
                editDateMillis = Date().time,
                toDoItemAction = ToDoItemAction.UPDATE
            )
        )
    }

    suspend fun getToDoItemById(toDoItemId: String): ToDoItem {
        return toDoItemLocalMapper.toToDoItem(
            toDoItemsLocalDataSource.getToDoItemLocal(toDoItemId)
        )
    }

    suspend fun updateToDoItem(
        toDoItemId: String,
        text: String,
        deadLineDate: Long?,
        priority: ToDoItemImportance
    ) {
        val toDoItemLocal = toDoItemsLocalDataSource.getToDoItemLocal(toDoItemId)
        val updatedToDoItemLocal = toDoItemLocal.copy(
            text = text,
            deadLineDateMillis = deadLineDate,
            importance = priority.value,
            toDoItemAction = ToDoItemAction.UPDATE,
            editDateMillis = Date().time
        )
        toDoItemsLocalDataSource.updateToDoItemLocal(updatedToDoItemLocal)
    }

    suspend fun getSynchronizationResult(): Result<Any?> {
        val toDoItemRemoteListResponse: ToDoItemRemoteListResponse

        try {
            toDoItemRemoteListResponse = toDoItemsRemoteDataSource.getToDoItemListRemote()
        } catch (exception: Exception) {
            return Result.failure(exception)
        }

        val currentDeviceRevision = sharedPreferences.getInt(LAST_KNOWN_REVISION_KEY, -1)

        if (currentDeviceRevision < toDoItemRemoteListResponse.revision) {
            val remoteList = toDoItemRemoteListResponse.list.map { toDoItemRemote ->
                toDoItemLocalMapper.map(toDoItemRemote)
            }

            toDoItemsLocalDataSource.updateLocalList(
                remoteList = remoteList
            )
        }

        applyNewRevision(toDoItemRemoteListResponse.revision)

        return try {
            updateRemoteToDoList()
            Result.success(null)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    private suspend fun updateRemoteToDoList() {
        val toDoItemsLocalToUpdate = toDoItemsLocalDataSource.getToDoItemLocalWithRemoteActionList()
        for (toDoItemLocal in toDoItemsLocalToUpdate) {
            updateRemoteToDoItem(toDoItemLocal)
        }
    }

    private suspend fun updateRemoteToDoItem(toDoItemLocal: ToDoItemLocal) {
        var responseException: Exception? = null
        try {
            val response = toDoItemsRemoteDataSource.getInundationResponse(
                toDoItemRemoteMapper.map(toDoItemLocal),
                toDoItemLocal.toDoItemAction!!
            )
            applyNewRevision(response.revision)
        } catch (exception: HttpException) {
            responseException = exception

        } finally {
            updateToDoItemLocalAfterRemoteAction(toDoItemLocal, responseException)
        }
    }

    private suspend fun updateToDoItemLocalAfterRemoteAction(
        toDoItemLocal: ToDoItemLocal,
        exception: Exception? = null
    ) {
        val isToDoItemLocalToUpdate = toDoItemLocal.toDoItemAction == ToDoItemAction.UPDATE
        if (exception?.message == "Not Found" && isToDoItemLocalToUpdate) {
            val updatedToDoItemLocal = toDoItemLocal.copy(toDoItemAction = ToDoItemAction.ADD)
            toDoItemsLocalDataSource.updateToDoItemLocal(updatedToDoItemLocal)
            return
        }

        if (toDoItemLocal.toDoItemAction != ToDoItemAction.DELETE) {
            val updatedToDoItemLocal = toDoItemLocal.copy(toDoItemAction = null)
            toDoItemsLocalDataSource.updateToDoItemLocal(updatedToDoItemLocal)
        } else {
            toDoItemsLocalDataSource.deleteToDoItemById(toDoItemLocal.id)
        }
    }

    suspend fun clearAll() {
        toDoItemsRemoteDataSource.clearRemoteList()
        toDoItemsLocalDataSource.clearList()
    }

    private fun applyNewRevision(revision: Int) {
        sharedPreferences.edit().putInt(LAST_KNOWN_REVISION_KEY, revision).apply()
    }
}
