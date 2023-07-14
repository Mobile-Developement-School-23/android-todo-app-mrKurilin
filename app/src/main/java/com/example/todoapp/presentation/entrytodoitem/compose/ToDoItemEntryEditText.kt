package com.example.todoapp.presentation.entrytodoitem.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.R

@Preview
@Composable
fun ToDoItemEntryEditText(
    text: String = "",
    onToDoItemEntryUIAction: (ToDoItemEntryUIAction) -> Unit = {},
) {
    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = 16.dp),
        value = text,
        onValueChange = {
            onToDoItemEntryUIAction(ToDoItemEntryUIAction.TextChanged(it))
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = GetThemeColors.colors.labelPrimary
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        minLines = 3,
        cursorBrush = SolidColor(GetThemeColors.colors.labelPrimary)
    ) { innerTextField ->

        Card(
            colors = CardDefaults.cardColors(
                containerColor = GetThemeColors.colors.backSecondary,
                contentColor = GetThemeColors.colors.labelTertiary
            )
        ) {

            Box(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                if (text.isEmpty())
                    Text(text = stringResource(id = R.string.what_need_to_do))
                innerTextField.invoke()
            }
        }
    }
}