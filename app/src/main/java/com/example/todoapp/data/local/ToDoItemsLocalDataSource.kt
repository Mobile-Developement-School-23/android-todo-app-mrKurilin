package com.example.todoapp.data.local

import com.example.todoapp.data.local.model.ToDoItemAction
import com.example.todoapp.data.local.model.ToDoItemLocal
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.random.Random

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
    }

    private suspend fun getToDoItemsWithoutRemoteActions(): List<ToDoItemLocal> {
        return toDoItemLocalDao.getToDoItemsWithoutRemoteActions()
    }

    suspend fun getToDoItemsToUpdateRemote(): List<ToDoItemLocal> {
        return toDoItemLocalDao.getToDoItemsToUpdateRemote()
    }

    suspend fun clearList() {
        toDoItemLocalDao.getToDoItemIdList().forEach {
            deleteToDoItemById(it)
        }
    }

    private suspend fun fillListForExample() {
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