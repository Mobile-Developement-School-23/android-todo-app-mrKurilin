package com.example.todoapp.presentation.entrytodoitem.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.todoapp.R

@Composable
fun DeleteTextBlock(
    isEnabled: Boolean = false
) {
    val color = if (isEnabled) {
        GetThemeColors.colors.red
    } else {
        GetThemeColors.colors.gray
    }
    Row {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(id = R.string.delete_to_do_item),
            tint = color,
        )

        Text(
            text = stringResource(id = R.string.delete),
            color = color,
        )
    }
}