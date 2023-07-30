package com.example.todoapp.presentation.entrytodoitem.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.R

@Preview
@Composable
fun ToDoItemEntryDeadLineBlock(
    currentDate: String = "",
    onToDoItemEntryUIAction: (ToDoItemEntryUIAction) -> Unit = {},
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column() {
            Text(text = stringResource(id = R.string.do_before_date))

            if (currentDate.isNotEmpty()) {
                Text(text = currentDate)
            }
        }

        Switch(
            checked = currentDate.isNotEmpty(),
            onCheckedChange = {
                onToDoItemEntryUIAction(ToDoItemEntryUIAction.DeadLineSwitchStateChanged)
            }
        )
    }
}