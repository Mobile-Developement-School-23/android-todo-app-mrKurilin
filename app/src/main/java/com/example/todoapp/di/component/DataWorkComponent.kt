package com.example.todoapp.di.component

import com.example.todoapp.di.scope.DataWorkScope
import com.example.todoapp.presentation.entrytodoitem.ToDoItemEntryViewModel
import com.example.todoapp.presentation.todolist.ToDoListViewModel
import dagger.Subcomponent

@DataWorkScope
@Subcomponent
interface DataWorkComponent {

    fun toDoItemEntryViewModel(): ToDoItemEntryViewModel

    fun toDoListViewModel(): ToDoListViewModel
}