package com.example.todoapp.data

import android.content.SharedPreferences
import com.example.todoapp.data.local.ToDoItemsLocalDataSource
import com.example.todoapp.data.local.mapper.ToDoItemLocalMapper
import com.example.todoapp.data.local.model.ToDoItemAction
import com.example.todoapp.data.remote.ToDoItemsRemoteDataSource
import com.example.todoapp.data.remote.lastKnownRevision
import com.example.todoapp.data.remote.mapper.ToDoItemRemoteMapper
import com.example.todoapp.data.remote.model.EntryToDoItemRemoteRequest
import com.example.todoapp.domain.model.ToDoItem
import com.example.todoapp.domain.model.ToDoItemImportance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
        try {
            val toDoItemRemoteResponse = toDoItemsRemoteDataSource.addToDoItemRemote(
                EntryToDoItemRemoteRequest(
                    toDoItemRemoteMapper.map(toDoItem)
                )
            )

            applyNewRevision(toDoItemRemoteResponse.revision)

            toDoItemsLocalDataSource.addToDoItem(toDoItemLocalMapper.map(toDoItem))
        } catch (exception: Exception) {
            val toDoItemLocal = toDoItemLocalMapper.map(
                toDoItem = toDoItem,
                toDoItemAction = ToDoItemAction.ADD
            )
            toDoItemsLocalDataSource.addToDoItem(toDoItemLocal)
        }
    }

    suspend fun deleteToDoItemById(toDoItemId: String) {
        try {
            val toDoItemRemoteResponse = toDoItemsRemoteDataSource.deleteToDoItemRemoteById(
                toDoItemId
            )

            applyNewRevision(toDoItemRemoteResponse.revision)

            toDoItemsLocalDataSource.deleteToDoItemById(toDoItemId)
        } catch (exception: Exception) {
            toDoItemsLocalDataSource.updateToDoItemLocal(
                toDoItemsLocalDataSource.getToDoItemLocal(toDoItemId).copy(
                    toDoItemAction = ToDoItemAction.DELETE
                )
            )
        }
    }

    suspend fun setDoneToDoItem(toDoItemId: String) {
        val toDoItemLocal = toDoItemsLocalDataSource.getToDoItemLocal(toDoItemId)
        val updatedToDoItemLocal = toDoItemsLocalDataSource.getToDoItemLocal(toDoItemId).copy(
            isDone = !toDoItemLocal.isDone
        )

        try {
            val toDoItemRemoteResponse = toDoItemsRemoteDataSource.updateToDoItemRemote(
                EntryToDoItemRemoteRequest(
                    toDoItemRemoteMapper.map(updatedToDoItemLocal)
                )
            )

            applyNewRevision(toDoItemRemoteResponse.revision)

            toDoItemsLocalDataSource.deleteToDoItemById(toDoItemId)
        } catch (exception: Exception) {
            toDoItemsLocalDataSource.updateToDoItemLocal(
                updatedToDoItemLocal.copy(
                    toDoItemAction = ToDoItemAction.UPDATE
                )
            )
        }
    }

    fun getToDoItemById(toDoItemId: String): ToDoItem {
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
        val toDoItemLocal = toDoItemsLocalDataSource.getToDoItemLocal(toDoItemId).copy(
            text = text,
            deadLineDateMillis = deadLineDate,
            importance = priority.value
        )
        try {
            val toDoItemRemoteResponse = toDoItemsRemoteDataSource.updateToDoItemRemote(
                EntryToDoItemRemoteRequest(
                    toDoItemRemoteMapper.map(toDoItemLocal)
                )
            )

            applyNewRevision(toDoItemRemoteResponse.revision)

            toDoItemsLocalDataSource.deleteToDoItemById(toDoItemId)
        } catch (exception: Exception) {
            toDoItemsLocalDataSource.updateToDoItemLocal(
                toDoItemLocal.copy(
                    toDoItemAction = ToDoItemAction.UPDATE
                )
            )
        }
    }

    suspend fun updateData() {
        val toDoItemRemoteListResponse = toDoItemsRemoteDataSource.getToDoItemListRemote()
        val currentDeviceRevision = sharedPreferences.getInt(lastKnownRevision, -1)

        if (currentDeviceRevision < toDoItemRemoteListResponse.revision) {
            toDoItemsLocalDataSource.updateLocalList(
                remoteList = toDoItemRemoteListResponse.list.map { toDoItemRemote ->
                    toDoItemLocalMapper.map(toDoItemRemote)
                },
                revision = toDoItemRemoteListResponse.revision
            )
        }

        toDoItemsLocalDataSource.getToDoItemsToUpdateRemote().forEach { toDoItemLocal ->
            val toDoItemRemote = toDoItemRemoteMapper.map(toDoItemLocal)

            val response = when (toDoItemLocal.toDoItemAction!!) {
                ToDoItemAction.UPDATE -> {
                    toDoItemsRemoteDataSource.updateToDoItemRemote(
                        EntryToDoItemRemoteRequest(toDoItemRemote)
                    )
                }

                ToDoItemAction.DELETE -> {
                    toDoItemsRemoteDataSource.deleteToDoItemRemoteById(
                        toDoItemLocal.id
                    )
                }

                ToDoItemAction.ADD -> {
                    toDoItemsRemoteDataSource.addToDoItemRemote(
                        EntryToDoItemRemoteRequest(toDoItemRemote)
                    )
                }
            }

            if (toDoItemLocal.toDoItemAction != ToDoItemAction.DELETE) {
                toDoItemsLocalDataSource.updateToDoItemLocal(
                    toDoItemLocal.copy(toDoItemAction = null)
                )
            } else {
                toDoItemsLocalDataSource.deleteToDoItemById(toDoItemLocal.id)
            }

            sharedPreferences.edit().putInt(lastKnownRevision, response.revision).apply()
        }
    }

    private fun applyNewRevision(revision: Int) {
        sharedPreferences.edit().putInt(
            lastKnownRevision,
            revision
        ).apply()
    }
}
