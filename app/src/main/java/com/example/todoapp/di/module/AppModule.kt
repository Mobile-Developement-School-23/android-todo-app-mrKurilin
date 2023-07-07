package com.example.todoapp.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.todoapp.data.local.SHARED_PREF_NAME
import com.example.todoapp.di.scope.AppScope
import dagger.Module
import dagger.Provides

@Module
interface AppModule {

    companion object {
        @AppScope
        @Provides
        fun provideSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(SHARED_PREF_NAME, Application.MODE_PRIVATE)
        }
    }
}