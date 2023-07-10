package com.example.todoapp.di.module

import android.content.Context
import android.net.ConnectivityManager
import com.example.todoapp.data.remote.AuthorizationInterceptor
import com.example.todoapp.data.remote.RetryInterceptor
import com.example.todoapp.data.remote.ToDoApiService
import com.example.todoapp.di.scope.AppScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Providing dependencies related to remote data access and network operations.
 */
@Module
interface RemoteDataModule {

    companion object {

        @AppScope
        @Provides
        fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        @AppScope
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

        @AppScope
        @Provides
        fun toDoApiService(
            okHttpClient: OkHttpClient,
        ): ToDoApiService {
            return Retrofit.Builder()
                .baseUrl("https://beta.mrdekk.ru/todobackend/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ToDoApiService::class.java)
        }

        @AppScope
        @Provides
        fun provideConnectivityManager(
            context: Context
        ): ConnectivityManager {
            return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }
    }
}