package com.example.todoapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.todoapp.data.local.ToDoItemLocalDao
import com.example.todoapp.data.local.ToDoItemsDatabase
import com.example.todoapp.data.remote.AuthorizationInterceptor
import com.example.todoapp.data.remote.RetryInterceptor
import com.example.todoapp.data.remote.ToDoApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthorizationInterceptor,
        retryInterceptor: RetryInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(retryInterceptor)
            .build()
    }

    @Provides
    fun toDoApiService(
        okHttpClient: OkHttpClient
    ): ToDoApiService {
        return Retrofit.Builder()
            .baseUrl("https://beta.mrdekk.ru/todobackend/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ToDoApiService::class.java)
    }

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            "shared preferences",
            Application.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun provideFilmLocalDao(context: Context): ToDoItemLocalDao = Room.databaseBuilder(
        context,
        ToDoItemsDatabase::class.java, "database-name"
    ).allowMainThreadQueries().build().toDoItemsLocalDao()
}