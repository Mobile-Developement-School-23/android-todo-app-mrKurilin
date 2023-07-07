package com.example.todoapp

import android.app.Application
import android.provider.Settings
import android.provider.Settings.Secure.ANDROID_ID
import com.example.todoapp.data.CurrentDeviceId
import com.example.todoapp.di.component.AppComponent
import com.example.todoapp.di.component.DaggerAppComponent

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
    }

    fun provideAppComponent(): AppComponent {
        return appComponent
    }
}