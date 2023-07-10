package com.example.todoapp.di.component

import android.content.Context
import com.example.todoapp.data.CurrentDeviceId
import com.example.todoapp.data.ToDoItemsRepository
import com.example.todoapp.di.module.LocalDataModule
import com.example.todoapp.di.module.RemoteDataModule
import com.example.todoapp.di.scope.AppScope
import com.example.todoapp.presentation.login.LoginViewModel
import dagger.BindsInstance
import dagger.Component

/**
 * Managing the dependency graph of the application.
 */
@AppScope
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

    fun toDoItemsRepository(): ToDoItemsRepository

    fun dataWorkComponent(): DataWorkComponent

    fun loginViewModel(): LoginViewModel
}
