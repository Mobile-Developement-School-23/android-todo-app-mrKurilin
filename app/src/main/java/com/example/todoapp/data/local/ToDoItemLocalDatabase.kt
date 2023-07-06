package com.example.todoapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoapp.data.local.model.ToDoItemLocal

/**
 * Provides access to the database and its associated DAO.
 */
@Database(
    entities = [ToDoItemLocal::class], version = 1
)
@TypeConverters(Converters::class)
abstract class ToDoItemLocalDatabase : RoomDatabase() {

    abstract fun toDoItemsLocalDao(): ToDoItemLocalDao
}