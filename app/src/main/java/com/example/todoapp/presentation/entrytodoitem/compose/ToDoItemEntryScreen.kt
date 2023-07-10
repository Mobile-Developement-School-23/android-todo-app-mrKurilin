package com.example.todoapp.presentation.entrytodoitem.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.presentation.entrytodoitem.ToDoItemEntryViewModel

@Preview
@Composable
fun ToDoItemEntryScreen(
    toDoItemId: String? = null,
    viewModel: ToDoItemEntryViewModel? = null
) {
    val textState = viewModel?.toDoItemEntryUIStateMutableStateFlow?.collectAsState()
    LazyColumn(
        modifier = Modifier
            .padding(8.dp)
//            .background(GetThemeColors.colors.backPrimary)
            .fillMaxSize(),
        content = {
            item {
                Row(
                    content = {
                        Image(painter = painterResource(id = R.drawable.close),
                            contentDescription = null,
                            modifier = Modifier
                                .clickable {
                                    viewModel?.onClosePressed()
                                }
                                .size(48.dp)
                        )
                        Button(
                            onClick = {
                                viewModel?.onSavePressed(toDoItemId)
                            },
                            modifier = Modifier
                                .wrapContentWidth()
                                .align(Alignment.Top),
                            content = {
                                Text(text = "Сохранить")
                            }
                        )
                    },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .padding(horizontal = 16.dp),
                    value = textState?.value?.toDoItemUIModel?.text ?: "",
                    onValueChange = { viewModel?.textChanged(it) },
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
                            if (textState?.value?.toDoItemUIModel?.text?.isEmpty() != false)
                                Text(text = stringResource(id = R.string.what_need_to_do))
                            innerTextField.invoke()
                        }
                    }
                }
            }
            item {
                Text(
                    text = "Важность",
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
            item {
                Text(text = "Влажность")
            }
        },
    )
}
