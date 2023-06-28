package com.example.todoapp

import android.app.Application
import android.provider.Settings
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.todoapp.data.CurrentDeviceId
import com.example.todoapp.data.LocalListUpdateWorker
import com.example.todoapp.data.ToDoItemsRepository
import com.example.todoapp.di.AppComponent
import com.example.todoapp.di.DaggerAppComponent
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ToDoApp : Application() {

    @Inject
    lateinit var todoItemsRepository: ToDoItemsRepository

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(
            context = this,
            currentDeviceId = CurrentDeviceId(Settings.Secure.ANDROID_ID)
        )

        val updateInterval = 8L // hours

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<LocalListUpdateWorker>(
            updateInterval,
            TimeUnit.HOURS
        ).setConstraints(constraints).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "LocalListUpdateWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}