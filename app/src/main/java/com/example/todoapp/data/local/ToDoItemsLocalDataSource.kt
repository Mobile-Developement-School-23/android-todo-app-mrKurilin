package com.example.todoapp.data.local

import android.content.SharedPreferences
import com.example.todoapp.data.local.model.ToDoItemAction
import com.example.todoapp.data.local.model.ToDoItemLocal
import com.example.todoapp.data.remote.lastKnownRevision
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.random.Random

class ToDoItemsLocalDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val toDoItemLocalDao: ToDoItemLocalDao,
) {

    init {
        if (toDoItemLocalDao.getToDoItemIdList().isEmpty()) {
            fillListForExample()
        }
    }

    fun getToDoItemListFlow(): Flow<List<ToDoItemLocal>> {
        return toDoItemLocalDao.getToDoItemLocalListFlow()
    }

    fun addToDoItem(toDoItemLocal: ToDoItemLocal) {
        toDoItemLocalDao.insertToDoItemLocal(toDoItemLocal)
    }

    fun deleteToDoItemById(id: String) {
        toDoItemLocalDao.deleteToDoItemById(id)
    }

    fun getToDoItemLocal(toDoItemId: String): ToDoItemLocal {
        return toDoItemLocalDao.getToDoItemLocalById(toDoItemId)
    }

    fun updateToDoItemLocal(toDoItemLocal: ToDoItemLocal) {
        toDoItemLocalDao.updateToDoItemLocal(toDoItemLocal)
    }

    fun updateLocalList(remoteList: List<ToDoItemLocal>, revision: Int) {
        val remoteToItemsById = remoteList.associateBy { it.id }
        val localToItemsWithoutRemoteActionsById = getToDoItemsWithoutRemoteActions().associateBy {
            it.id
        }

        localToItemsWithoutRemoteActionsById.keys.subtract(remoteToItemsById.keys).forEach { id ->
            toDoItemLocalDao.deleteToDoItemById(id)
        }

        remoteToItemsById.keys.subtract(localToItemsWithoutRemoteActionsById.keys).forEach { id ->
            val toDoItemLocal = remoteToItemsById[id]
            if (toDoItemLocal != null) toDoItemLocalDao.insertToDoItemLocal(toDoItemLocal)
        }

        for (id in remoteToItemsById.keys.intersect(localToItemsWithoutRemoteActionsById.keys)) {
            val toDoItemLocalFromRemote = remoteToItemsById[id] ?: continue
            val localToDoItemLocal = localToItemsWithoutRemoteActionsById[id] ?: continue

            if (localToDoItemLocal.editDateMillis < toDoItemLocalFromRemote.editDateMillis) {
                toDoItemLocalDao.updateToDoItemLocal(toDoItemLocalFromRemote)
            }
        }

        sharedPreferences.edit().putInt(lastKnownRevision, revision).apply()
    }

    private fun getToDoItemsWithoutRemoteActions(): List<ToDoItemLocal> {
        return toDoItemLocalDao.getToDoItemsWithoutRemoteActions()
    }

    fun getToDoItemsToUpdateRemote(): List<ToDoItemLocal> {
        return toDoItemLocalDao.getToDoItemsToUpdateRemote()
    }

    private fun clearList() {
        toDoItemLocalDao.getToDoItemIdList().forEach {
            deleteToDoItemById(it)
        }
    }

    private fun fillListForExample() {
        addToDoItem(
            ToDoItemLocal(
                id = Random.nextInt().toString(),
                text = "Купить что-то",
                isDone = false,
                creationDateMillis = 0,
                editDateMillis = 0,
                importance = 0,
                deadLineDateMillis = null,
                toDoItemAction = ToDoItemAction.ADD,
            )
        )

        addToDoItem(
            ToDoItemLocal(
                id = Random.nextInt().toString(),
                text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно",
                isDone = false,
                creationDateMillis = 0,
                editDateMillis = 0,
                importance = Random.nextInt(0, 2),
                deadLineDateMillis = null,
                toDoItemAction = ToDoItemAction.ADD,
            )
        )

        addToDoItem(
            ToDoItemLocal(
                id = Random.nextInt().toString(),
                text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрезается текст при количестве строк более трёх",
                isDone = false,
                creationDateMillis = 0,
                editDateMillis = 0,
                importance = Random.nextInt(0, 2),
                deadLineDateMillis = null,
                toDoItemAction = ToDoItemAction.ADD,
            )
        )

        addToDoItem(
            ToDoItemLocal(
                id = Random.nextInt().toString(),
                text = "Купить что-то с низким приоритетом",
                isDone = false,
                creationDateMillis = 0,
                editDateMillis = 0,
                importance = 0,
                deadLineDateMillis = null,
                toDoItemAction = ToDoItemAction.ADD,
            )
        )

        addToDoItem(
            ToDoItemLocal(
                id = Random.nextInt().toString(),
                text = "Купить что-то с высоким приоритетом",
                isDone = false,
                creationDateMillis = 0,
                editDateMillis = 0,
                importance = 2,
                deadLineDateMillis = null,
                toDoItemAction = ToDoItemAction.ADD,
            )
        )

        addToDoItem(
            ToDoItemLocal(
                id = Random.nextInt().toString(),
                text = "Выполненная задача",
                isDone = true,
                creationDateMillis = 0,
                editDateMillis = 0,
                importance = 1,
                deadLineDateMillis = null,
                toDoItemAction = ToDoItemAction.ADD,
            )
        )

        addToDoItem(
            ToDoItemLocal(
                id = Random.nextInt().toString(),
                text = "Задача с дедлайном",
                isDone = true,
                creationDateMillis = 0,
                editDateMillis = 0,
                importance = 1,
                deadLineDateMillis = 1686766582536,
                toDoItemAction = ToDoItemAction.ADD,
            )
        )

        addToDoItem(
            ToDoItemLocal(
                id = Random.nextInt().toString(),
                text = "Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела ",
                isDone = false,
                creationDateMillis = 0,
                editDateMillis = 0,
                importance = 1,
                deadLineDateMillis = 1686766582536,
                toDoItemAction = ToDoItemAction.ADD,
            )
        )

        repeat(10) {
            addToDoItem(
                ToDoItemLocal(
                    id = Random.nextInt().toString(),
                    text = Random.nextLong().toString(),
                    isDone = Random.nextBoolean(),
                    creationDateMillis = 0,
                    editDateMillis = 0,
                    importance = Random.nextInt(0, 2),
                    deadLineDateMillis = Random.nextLong(),
                    toDoItemAction = ToDoItemAction.ADD,
                )
            )
        }
    }
}