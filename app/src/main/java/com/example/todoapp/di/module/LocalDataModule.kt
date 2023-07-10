package com.example.todoapp.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.todoapp.data.local.SHARED_PREF_NAME
import com.example.todoapp.data.local.ToDoItemLocalDao
import com.example.todoapp.data.local.ToDoItemLocalDatabase
import com.example.todoapp.di.scope.AppScope
import dagger.Module
import dagger.Provides

/**
 * Providing dependencies related to local data storage and access.
 */
@Module
interface LocalDataModule {

    companion object {

        @AppScope
        @Provides
        fun provideFilmLocalDao(context: Context): ToDoItemLocalDao {
            return Room.databaseBuilder(
                context,
                ToDoItemLocalDatabase::class.java,
                "database-name"
            ).allowMainThreadQueries().build().toDoItemsLocalDao()
        }

        @AppScope
        @Provides
        fun provideSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(SHARED_PREF_NAME, Application.MODE_PRIVATE)
        }
    }
}