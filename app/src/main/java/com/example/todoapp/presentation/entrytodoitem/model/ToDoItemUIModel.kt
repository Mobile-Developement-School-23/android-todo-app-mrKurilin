package com.example.todoapp.presentation.entrytodoitem.model

import androidx.annotation.StringRes
import com.example.todoapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 *  Represents the UI model for a ToDoItem used in the entry screen.
 */
data class ToDoItemUIModel(
    val id: String? = null,
    val text: String = "",
    @StringRes
    val priorityStringId: Int = R.string.basic,
    val deadLineDateMillis: Long? = null,
) {

    val deadLineDateString: String
        get(): String = if (deadLineDateMillis == null) {
            ""
        } else {
            SimpleDateFormat(
                "dd MMMM yyyy",
                Locale.getDefault()
            ).format(Date(deadLineDateMillis))
        }
}