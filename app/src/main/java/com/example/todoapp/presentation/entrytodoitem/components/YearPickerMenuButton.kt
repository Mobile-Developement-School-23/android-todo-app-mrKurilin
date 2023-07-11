package com.example.todoapp.presentation.entrytodoitem.components

import android.widget.CalendarView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date

val text = MutableStateFlow(" XXX ")

@Preview
@Composable
fun Test() {
    LazyColumn(content = {
        item {
            AndroidView(
                { CalendarView(it) },
                modifier = Modifier.fillMaxWidth(),
                update = { views ->
                    views.date = Date().time
                    views.setOnDateChangeListener { _, y, m, d ->
                        text.value = "$y $m $d"
                    }
                }
            )
        }
        item {
            Text(text = text.collectAsState().value)
        }
    }
    )
}