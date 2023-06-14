package com.example.todoapp

import android.app.Application
import android.content.SharedPreferences
import com.example.todoapp.data.TodoItemsRepository
import com.example.todoapp.data.local.ToDoItemLocalMapper
import com.example.todoapp.data.local.ToDoItemsLocalDataSource
import com.example.todoapp.presentation.to_do_list.model.ToDoItemUIMapper

class ToDoApp : Application() {

    private lateinit var sharedPreferences: SharedPreferences

    private val toDoItemsLocalDataSource by lazy {
        ToDoItemsLocalDataSource(sharedPreferences)
    }

    private val todoItemsRepository by lazy {
        TodoItemsRepository(
            toDoItemsLocalDataSource,
            ToDoItemLocalMapper()
        )
    }

    private val toDoItemUIMapper by lazy {
        ToDoItemUIMapper()
    }

    override fun onCreate() {
        super.onCreate()

        sharedPreferences = getSharedPreferences(
            "shared preferences",
            MODE_PRIVATE
        )
    }

    fun provideTodoItemsRepository(): TodoItemsRepository {
        return todoItemsRepository
    }

    fun provideToDoItemUIMapper(): ToDoItemUIMapper {
        return toDoItemUIMapper
    }
}