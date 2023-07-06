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
import com.example.todoapp.di.AppComponent
import com.example.todoapp.di.DaggerAppComponent
import java.util.concurrent.TimeUnit

const val UPDATE_PERIOD_HOURS = 8L
const val FLEX_UPDATE_PERIOD_HOURS = 1L

class ToDoApp : Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(
            context = this,
            currentDeviceId = CurrentDeviceId(Settings.Secure.ANDROID_ID)
        )

        enqueueLocalListUpdateWorker()
    }

    fun provideAppComponent(): AppComponent {
        return appComponent
    }

    private fun enqueueLocalListUpdateWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<LocalListUpdateWorker>(
            UPDATE_PERIOD_HOURS,
            TimeUnit.HOURS,
            FLEX_UPDATE_PERIOD_HOURS,
            TimeUnit.HOURS,
        ).setConstraints(constraints).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "LocalListUpdateWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}