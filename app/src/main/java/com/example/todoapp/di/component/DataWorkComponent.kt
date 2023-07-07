package com.example.todoapp.di.component

import com.example.todoapp.data.ToDoItemsRepository
import com.example.todoapp.di.module.LocalDataModule
import com.example.todoapp.di.module.RemoteDataModule
import com.example.todoapp.di.scope.DataWorkScope
import com.example.todoapp.presentation.entrytodoitem.ToDoItemEntryViewModel
import com.example.todoapp.presentation.todolist.ToDoListViewModel
import dagger.Subcomponent

@DataWorkScope
@Subcomponent(
    modules = [
        LocalDataModule::class,
        RemoteDataModule::class
    ]
)
interface DataWorkComponent {

    fun toDoItemsRepository(): ToDoItemsRepository

    fun toDoItemEntryViewModel(): ToDoItemEntryViewModel

    fun toDoListViewModel(): ToDoListViewModel
}