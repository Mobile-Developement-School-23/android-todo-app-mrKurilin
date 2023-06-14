package com.example.todoapp.data.local

import android.content.SharedPreferences
import com.example.todoapp.data.local.model.ToDoItemLocal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date
import kotlin.random.Random

private const val SHARED_PREF_LIST_KEY = "SHARED_PREF_LIST_KEY"

class ToDoItemsLocalDataSource(
    private val sharedPreferences: SharedPreferences,
) {

    private val gson = Gson()
    private val type = object : TypeToken<List<ToDoItemLocal>>() {}.type

    private val toDoListMutableStateFlow = MutableStateFlow(
        try {
            gson.fromJson(
                sharedPreferences.getString(SHARED_PREF_LIST_KEY, ""),
                type
            ) as List<ToDoItemLocal>
        } catch (exception: NullPointerException) {
            listOf()
        }
    )

    init {
        if (toDoListMutableStateFlow.value.isEmpty()) {
            fillListForExample()
        }
    }

    fun getToDoItemListStateFlow(): StateFlow<List<ToDoItemLocal>> {
        return toDoListMutableStateFlow.asStateFlow()
    }

    fun addToDoItem(toDoItemLocal: ToDoItemLocal) {
        val list = toDoListMutableStateFlow.value.toMutableList()
        list.add(toDoItemLocal)
        toDoListMutableStateFlow.value = list
        sharedPreferences.edit().putString(
            SHARED_PREF_LIST_KEY,
            gson.toJson(list)
        ).apply()
    }

    fun deleteToDoItemById(id: String) {
        val list = toDoListMutableStateFlow.value.toMutableList()
        list.removeIf { it.id == id }
        toDoListMutableStateFlow.value = list
        sharedPreferences.edit().putString(
            SHARED_PREF_LIST_KEY,
            gson.toJson(list)
        ).apply()
    }

    fun setDoneToDoItem(toDoItemId: String) {
        val list = toDoListMutableStateFlow.value.toMutableList()
        val toDoItemLocal = list.first { it.id == toDoItemId }
        val isDone = toDoItemLocal.isDone
        val newToDoItem = toDoItemLocal.copy(isDone = !isDone)
        list[list.indexOf(toDoItemLocal)] = newToDoItem
        updateLocalList(list)
    }

    fun getToDoItem(toDoItemId: String): ToDoItemLocal {
        return toDoListMutableStateFlow.value.first { it.id == toDoItemId }
    }

    fun updateToDoItemLocal(toDoItemLocal: ToDoItemLocal) {
        val list = toDoListMutableStateFlow.value.toMutableList()
        list[list.indexOfFirst { it.id == toDoItemLocal.id }] = toDoItemLocal.copy(
            editDateMillis = Date().time
        )
        updateLocalList(list)
    }

    private fun updateLocalList(list: List<ToDoItemLocal>) {
        toDoListMutableStateFlow.value = list
        sharedPreferences.edit().putString(
            SHARED_PREF_LIST_KEY,
            gson.toJson(list)
        ).apply()
    }

    private fun clearList() {
        toDoListMutableStateFlow.value.forEach {
            deleteToDoItemById(it.id)
        }
    }

    private fun fillListForExample() {
        addToDoItem(
            ToDoItemLocal(
                id = Random.nextInt().toString(),
                text = "Купить что-то",
                isDone = false,
                creationDateMillis = 0,
                editDateMillis = null,
                priorityInt = 0,
                deadLineDateMillis = null,
            )
        )

        addToDoItem(
            ToDoItemLocal(
                id = Random.nextInt().toString(),
                text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно",
                isDone = false,
                creationDateMillis = 0,
                editDateMillis = null,
                priorityInt = Random.nextInt(0, 2),
                deadLineDateMillis = null,
            )
        )

        addToDoItem(
            ToDoItemLocal(
                id = Random.nextInt().toString(),
                text = "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрезается текст при количестве строк более трёх",
                isDone = false,
                creationDateMillis = 0,
                editDateMillis = null,
                priorityInt = Random.nextInt(0, 2),
                deadLineDateMillis = null,
            )
        )

        addToDoItem(
            ToDoItemLocal(
                id = Random.nextInt().toString(),
                text = "Купить что-то с низким приоритетом",
                isDone = false,
                creationDateMillis = 0,
                editDateMillis = null,
                priorityInt = 0,
                deadLineDateMillis = null,
            )
        )

        addToDoItem(
            ToDoItemLocal(
                id = Random.nextInt().toString(),
                text = "Купить что-то с высоким приоритетом",
                isDone = false,
                creationDateMillis = 0,
                editDateMillis = null,
                priorityInt = 2,
                deadLineDateMillis = null,
            )
        )

        addToDoItem(
            ToDoItemLocal(
                id = Random.nextInt().toString(),
                text = "Выполненная задача",
                isDone = true,
                creationDateMillis = 0,
                editDateMillis = null,
                priorityInt = 1,
                deadLineDateMillis = null,
            )
        )

        addToDoItem(
            ToDoItemLocal(
                id = Random.nextInt().toString(),
                text = "Задача с дедлайном",
                isDone = true,
                creationDateMillis = 0,
                editDateMillis = null,
                priorityInt = 1,
                deadLineDateMillis = 1686766582536,
            )
        )

        addToDoItem(
            ToDoItemLocal(
                id = Random.nextInt().toString(),
                text = "Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела Огромное количество слов для проверки фрагмента добавления дела ",
                isDone = false,
                creationDateMillis = 0,
                editDateMillis = null,
                priorityInt = 1,
                deadLineDateMillis = 1686766582536,
            )
        )

        repeat(10) {
            addToDoItem(
                ToDoItemLocal(
                    id = Random.nextInt().toString(),
                    text = Random.nextLong().toString(),
                    isDone = Random.nextBoolean(),
                    creationDateMillis = 0,
                    editDateMillis = null,
                    priorityInt = Random.nextInt(0,2),
                    deadLineDateMillis = Random.nextLong(),
                )
            )
        }
    }
}