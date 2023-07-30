package com.example.todoapp.presentation.entrytodoitem.compose

import android.widget.CalendarView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import java.util.Calendar

@Preview
@Composable
fun AnimatedVisibilityCalendarView(
    currentDate: Long? = null,
    onToDoItemEntryUIAction: (ToDoItemEntryUIAction) -> Unit = {},
) {

    AnimatedVisibility(
        visible = currentDate != null,
        enter = expandVertically(),
        exit = shrinkVertically(),
        modifier = Modifier.fillMaxWidth()
    ) {
        AndroidView(
            factory = {
                CalendarView(it)
            },
            update = { calendarView ->
                if (currentDate == null) {
                    return@AndroidView
                }
                calendarView.date = currentDate
                calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                    onToDoItemEntryUIAction(
                        ToDoItemEntryUIAction.SelectedDateChanged(
                            Calendar.getInstance().also {
                                it.set(year, month, dayOfMonth, 0, 0, 0)
                                it.set(Calendar.MILLISECOND, 0)
                            }.timeInMillis
                        )
                    )
                }
            }
        )
    }
}