package com.example.todoapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.todoapp.data.local.SharedPreferencesConst.Companion.SHARED_PREF_NAME
import com.example.todoapp.data.local.ToDoItemLocalDao
import com.example.todoapp.data.local.ToDoItemsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface LocalDataModule {

    companion object {

        @Provides
        fun provideSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(
                SHARED_PREF_NAME,
                Application.MODE_PRIVATE
            )
        }

        @Singleton
        @Provides
        fun provideFilmLocalDao(context: Context): ToDoItemLocalDao {
            return Room.databaseBuilder(
                context,
                ToDoItemsDatabase::class.java,
                "database-name"
            ).allowMainThreadQueries().build().toDoItemsLocalDao()
        }
    }
}