package com.example.todoapp.presentation.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

const val REMINDER_NOTIFICATION_REQUEST_CODE = 123
const val REMINDER_NOTIFICATION_CHANEL_ID = "REMINDER_NOTIFICATION_CHANEL_ID"
const val REMINDER_NOTIFICATION_CHANEL_NAME = "REMINDER_NOTIFICATION_CHANEL_NAME"

class ReminderManager @Inject constructor(
    private val context: Context
) {

    fun startReminder(
        reminderTime: String = "07:59",
        reminderId: Int = REMINDER_NOTIFICATION_REQUEST_CODE
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val (hours, min) = reminderTime.split(":").map { it.toInt() }
        val intent = Intent(
            context.applicationContext,
            AlarmReceiver::class.java
        ).let { intent ->
            PendingIntent.getBroadcast(
                context.applicationContext,
                reminderId,
                intent,
                PendingIntent.FLAG_MUTABLE
            )
        }

        val calendar: Calendar = Calendar.getInstance(Locale.getDefault()).apply {
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, min)
        }

        if (Calendar.getInstance(Locale.ENGLISH)
                .apply { add(Calendar.MINUTE, 1) }.timeInMillis - calendar.timeInMillis > 0
        ) {
            calendar.add(Calendar.DATE, 1)
        }

        try {
            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(calendar.timeInMillis, intent),
                intent
            )
        } catch (security: SecurityException) {
            error("Alarm set permission denied")
        }
    }

    fun stopReminder(
        reminderId: Int = REMINDER_NOTIFICATION_REQUEST_CODE
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context,
                reminderId,
                intent,
                PendingIntent.FLAG_MUTABLE
            )
        }
        alarmManager.cancel(intent)
    }
}