package com.example.todoapp.di

import android.content.Context
import com.example.todoapp.data.CurrentDeviceId
import com.example.todoapp.presentation.login_fragment.LoginViewModel
import com.example.todoapp.presentation.to_do_item_entry.ToDoItemEntryViewModel
import com.example.todoapp.presentation.to_do_list.ToDoListViewModel
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
}
