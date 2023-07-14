package com.example.todoapp.presentation.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.todoapp.R
import com.example.todoapp.ToDoApp
import com.example.todoapp.presentation.MainActivity
import java.util.Calendar
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.MILLISECOND
import java.util.Calendar.MINUTE
import java.util.Calendar.SECOND

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendReminderNotification(
            context = context,
            channelId = REMINDER_NOTIFICATION_CHANEL_ID
        )

        val toDoApp = context.applicationContext as ToDoApp
        toDoApp.provideAppComponent().reminderManager().startReminder()
    }
}

fun NotificationManager.sendReminderNotification(
    context: Context,
    channelId: String,
) {
    val applicationContext = context.applicationContext
    val toDoApp = applicationContext as ToDoApp
    val toDoItemsRepository = toDoApp.provideAppComponent().toDoItemsRepository()

    val today = Calendar.getInstance().also {
        it.set(HOUR_OF_DAY, 0)
        it.set(MINUTE, 0)
        it.set(SECOND, 0)
        it.set(MILLISECOND, 0)
    }.timeInMillis

    val deadLineToDoItems = toDoItemsRepository.getCurrentDeadLineToDoItems(today)

    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        1,
        contentIntent,
        PendingIntent.FLAG_MUTABLE
    )

    deadLineToDoItems.forEach { toDoItem ->
        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Дедлайн выполнения задачи")
            .setContentText(toDoItem.text)
            .setSmallIcon(R.drawable.delete)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notify(toDoItem.id.hashCode(), builder.build())
    }
}
