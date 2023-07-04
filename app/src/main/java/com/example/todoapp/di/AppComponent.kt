package com.example.todoapp.di

import android.content.Context
import com.example.todoapp.ToDoApp
import com.example.todoapp.data.CurrentDeviceId
import com.example.todoapp.presentation.entry_to_do_item_fragment.ToDoItemEntryViewModel
import com.example.todoapp.presentation.login_fragment.LoginViewModel
import com.example.todoapp.presentation.to_do_list_fragment.ToDoListViewModel
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
