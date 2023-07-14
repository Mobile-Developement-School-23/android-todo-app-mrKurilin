package com.example.todoapp.presentation.entrytodoitem.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.R

@Preview
@Composable
fun ToDoItemEntryToolBar(
    onToDoItemEntryUIAction: (ToDoItemEntryUIAction) -> Unit = {},
) {
    Row(
        content = {
            Image(painter = painterResource(id = R.drawable.close),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        onToDoItemEntryUIAction(ToDoItemEntryUIAction.CloseButtonPressed)
                    }
                    .size(48.dp)
            )
            Button(
                onClick = {
                    onToDoItemEntryUIAction(ToDoItemEntryUIAction.SaveButtonPressed)
                },
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.Top),
                content = {
                    Text(text = stringResource(id = R.string.save))
                }
            )
        },
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    )
}