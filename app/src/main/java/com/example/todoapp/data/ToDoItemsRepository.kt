package com.example.todoapp.data

import android.content.SharedPreferences
import com.example.todoapp.data.local.ToDoItemsLocalDataSource
import com.example.todoapp.data.local.mapper.ToDoItemLocalMapper
import com.example.todoapp.data.local.model.ToDoItemAction
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

        try {
            updateRemoteToDoList()
        } catch (exception: Exception) {
            return Result.failure(exception)
        }

        return Result.success(null)
    }

    private suspend fun updateRemoteToDoList() {
        toDoItemsLocalDataSource.getToDoItemsToUpdateRemote().forEach { toDoItemLocal ->
            val toDoItemRemote = toDoItemRemoteMapper.map(toDoItemLocal)

            try {
                val response = toDoItemsRemoteDataSource.getInundationResponse(
                    toDoItemRemote,
                    toDoItemLocal.toDoItemAction!!
                )
                applyNewRevision(response.revision)
            } catch (exception: HttpException) {
                if (exception.message == "Not Found") {
                    if (toDoItemLocal.toDoItemAction == ToDoItemAction.DELETE) {
                        toDoItemsLocalDataSource.deleteToDoItemById(toDoItemLocal.id)
                    } else if (toDoItemLocal.toDoItemAction == ToDoItemAction.UPDATE) {
                        toDoItemsLocalDataSource.updateToDoItemLocal(
                            toDoItemLocal.copy(toDoItemAction = ToDoItemAction.ADD)
                        )
                    }
                }
            }

            if (toDoItemLocal.toDoItemAction != ToDoItemAction.DELETE) {
                toDoItemsLocalDataSource.updateToDoItemLocal(
                    toDoItemLocal.copy(toDoItemAction = null)
                )
            } else {
                toDoItemsLocalDataSource.deleteToDoItemById(toDoItemLocal.id)
            }
        }
    }

    suspend fun clearAll() {
        toDoItemsRemoteDataSource.clearRemoteList()
        toDoItemsLocalDataSource.clearList()
    }

    private fun applyNewRevision(revision: Int) {
        sharedPreferences.edit().putInt(
            LAST_KNOWN_REVISION_KEY,
            revision
        ).apply()
    }
}
