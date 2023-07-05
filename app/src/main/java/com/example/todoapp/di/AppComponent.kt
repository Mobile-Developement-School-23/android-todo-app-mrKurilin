package com.example.todoapp.di

import android.content.Context
import com.example.todoapp.ToDoApp
import com.example.todoapp.data.CurrentDeviceId
import com.example.todoapp.presentation.entrytodoitem.ToDoItemEntryViewModel
import com.example.todoapp.presentation.login.LoginViewModel
import com.example.todoapp.presentation.todolist.ToDoListViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        LocalDataModule::class,
        RemoteDataModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance
            context: Context,
            @BindsInstance
            currentDeviceId: CurrentDeviceId,
        ): AppComponent
    }

    fun toDoItemEntryViewModel(): ToDoItemEntryViewModel

    fun toDoListViewModel(): ToDoListViewModel

    fun loginViewModel(): LoginViewModel
    fun inject(toDoApp: ToDoApp)
}
