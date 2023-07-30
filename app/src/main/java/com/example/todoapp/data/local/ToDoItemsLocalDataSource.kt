package com.example.todoapp.data.local

import com.example.todoapp.data.DAY_MILLIS
import com.example.todoapp.data.local.model.ToDoItemLocal
import com.example.todoapp.data.local.model.ToDoItemLocalRemoteAction
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject
import kotlin.random.Random

const val EXAMPLE_TO_DO_ITEMS_COUNT = 20

/**
 * Provides access to the local data source for [ToDoItemLocal] and performs operations on the
 * local database.
 * Acts as a data source layer between the repository or use case layer and the local database.
 * Responsible for retrieving, adding, updating, and deleting to-do items in the local database.
 */
class ToDoItemsLocalDataSource @Inject constructor(
    private val toDoItemLocalDao: ToDoItemLocalDao,
) {

    fun getToDoItemListFlow(): Flow<List<ToDoItemLocal>> {
        return toDoItemLocalDao.getToDoItemLocalListFlow()
    }

    suspend fun addToDoItem(toDoItemLocal: ToDoItemLocal) {
        toDoItemLocalDao.insertToDoItemLocal(toDoItemLocal)
    }

    suspend fun deleteToDoItemById(id: String) {
        toDoItemLocalDao.deleteToDoItemById(id)
    }

    suspend fun getToDoItemLocal(toDoItemId: String): ToDoItemLocal {
        return toDoItemLocalDao.getToDoItemLocalById(toDoItemId)
    }

    suspend fun updateToDoItemLocal(toDoItemLocal: ToDoItemLocal) {
        toDoItemLocalDao.updateToDoItemLocal(toDoItemLocal)
    }

    suspend fun updateLocalList(remoteList: List<ToDoItemLocal>) {
        val remoteToItemsById = remoteList.associateBy { it.id }
        val noRemoteActionsToDoItemLocalList = getNoRemoteActionsToDoItemLocalList().associateBy {
            it.id
        }

        // delete remotely deleted to do items
        noRemoteActionsToDoItemLocalList.keys.subtract(remoteToItemsById.keys).forEach { id ->
            toDoItemLocalDao.deleteToDoItemById(id)
        }

        // add remotely added to do items
        remoteToItemsById.keys.subtract(noRemoteActionsToDoItemLocalList.keys).forEach { id ->
            toDoItemLocalDao.insertToDoItemLocal(remoteToItemsById[id]!!)
        }

        updateRemotelyUpdatedToDoItems(remoteToItemsById, noRemoteActionsToDoItemLocalList)
    }

    private suspend fun updateRemotelyUpdatedToDoItems(
        remoteToItemsById: Map<String, ToDoItemLocal>,
        noRemoteActionsToDoItemLocalList: Map<String, ToDoItemLocal>
    ) {
        for (id in remoteToItemsById.keys.intersect(noRemoteActionsToDoItemLocalList.keys)) {
            val toDoItemLocalFromRemote = remoteToItemsById[id]!!
            val toDoItemLocal = noRemoteActionsToDoItemLocalList[id]!!

            if (toDoItemLocal.editDateMillis < toDoItemLocalFromRemote.editDateMillis) {
                toDoItemLocalDao.updateToDoItemLocal(toDoItemLocalFromRemote)
            }
        }
    }

    suspend fun getToDoItemLocalWithRemoteActionList(): List<ToDoItemLocal> {
        return toDoItemLocalDao.toDoItemLocalWithRemoteActionList()
    }

    suspend fun clearList() {
        toDoItemLocalDao.getToDoItemIdList().forEach {
            deleteToDoItemById(it)
        }
    }

    private suspend fun getNoRemoteActionsToDoItemLocalList(): List<ToDoItemLocal> {
        return toDoItemLocalDao.noRemoteActionsToDoItemLocalList()
    }

    private suspend fun fillListForExample() {
        repeat(EXAMPLE_TO_DO_ITEMS_COUNT) {
            addRandomToDoItemLocal()
        }
    }

    private suspend fun addRandomToDoItemLocal() {
        addToDoItem(
            ToDoItemLocal(
                id = Random.nextInt().toString(),
                text = Random.nextDouble().toString(),
                isDone = Random.nextBoolean(),
                creationDateMillis = Random.nextLong(),
                importance = Random.nextInt(0, 2),
                deadLineEpochDay = Random.nextLong(),
                toDoItemLocalRemoteAction = ToDoItemLocalRemoteAction.ADD,
            )
        )
    }

    fun getCurrentDeadLineToDoItems(): List<ToDoItemLocal> {
        val currentEpochDay = Date().time / DAY_MILLIS
        return toDoItemLocalDao.getCurrentDeadLineToDoItems(currentEpochDay)
    }
}