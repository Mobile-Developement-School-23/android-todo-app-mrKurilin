package com.example.todoapp.di.module

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.local.ToDoItemLocalDao
import com.example.todoapp.data.local.ToDoItemLocalDatabase
import com.example.todoapp.di.scope.DataWorkScope
import dagger.Module
import dagger.Provides

/**
 * Providing dependencies related to local data storage and access.
 */
@Module
interface LocalDataModule {

    companion object {

        @DataWorkScope
        @Provides
        fun provideFilmLocalDao(context: Context): ToDoItemLocalDao {
            return Room.databaseBuilder(
                context,
                ToDoItemLocalDatabase::class.java,
                "database-name"
            ).allowMainThreadQueries().build().toDoItemsLocalDao()
        }
    }
}