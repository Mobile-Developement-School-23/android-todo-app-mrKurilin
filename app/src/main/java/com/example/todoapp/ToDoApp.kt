package com.example.todoapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.provider.Settings
import android.provider.Settings.Secure.ANDROID_ID
import androidx.core.content.ContextCompat
import com.example.todoapp.data.CurrentDeviceId
import com.example.todoapp.di.component.AppComponent
import com.example.todoapp.di.component.DaggerAppComponent
import com.example.todoapp.presentation.notification.REMINDER_NOTIFICATION_CHANEL_ID
import com.example.todoapp.presentation.notification.REMINDER_NOTIFICATION_CHANEL_NAME
import com.example.todoapp.presentation.notification.ReminderManager

class ToDoApp : Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(
            context = this,
            currentDeviceId = CurrentDeviceId(
                Settings.Secure.getString(contentResolver, ANDROID_ID)
            )
        )

        createNotificationsChannels()
        appComponent.reminderManager().startReminder()
    }

    fun provideAppComponent(): AppComponent {
        return appComponent
    }

    private fun createNotificationsChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                REMINDER_NOTIFICATION_CHANEL_ID,
                REMINDER_NOTIFICATION_CHANEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            ContextCompat.getSystemService(this, NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }
    }
}