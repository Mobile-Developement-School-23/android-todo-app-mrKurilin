package com.example.todoapp.presentation.entrytodoitem.compose

import ToDoItemEntryPriorityBottomSheet
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.presentation.entrytodoitem.ToDoItemEntryUIState

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ToDoItemEntryScreen(
    toDoItemEntryUIState: ToDoItemEntryUIState = ToDoItemEntryUIState(),
    onToDoItemEntryUIAction: (ToDoItemEntryUIAction) -> Unit = {},
) {

    val scope = rememberCoroutineScope()

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = false,
            initialValue = SheetValue.Hidden
        )
    )

    BottomSheetScaffold(
        topBar = {
            ToDoItemEntryToolBar(
                onToDoItemEntryUIAction = onToDoItemEntryUIAction,
                isSaveButtonEnabled = toDoItemEntryUIState.toDoItemUIModel.text.isNotBlank()
            )
        },
        scaffoldState = scaffoldState,
        sheetContent = {
            ToDoItemEntryPriorityBottomSheet(
                scaffoldState = scaffoldState,
                onToDoItemEntryUIAction = onToDoItemEntryUIAction,
                scope = scope
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            item {
                ToDoItemEntryEditText(
                    text = toDoItemEntryUIState.toDoItemUIModel.text,
                    onToDoItemEntryUIAction = onToDoItemEntryUIAction
                )

                Divider(modifier = Modifier.fillMaxWidth())

                PriorityColumn(
                    scaffoldState = scaffoldState,
                    priorityStringId = toDoItemEntryUIState.toDoItemUIModel.priorityStringId,
                    scope = scope,
                )

                Divider(modifier = Modifier.fillMaxWidth())

                ToDoItemEntryDeadLineBlock(
                    currentDate = toDoItemEntryUIState.toDoItemUIModel.deadLineDateString,
                    onToDoItemEntryUIAction = onToDoItemEntryUIAction,
                )

                AnimatedVisibilityCalendarView(
                    currentDate = toDoItemEntryUIState.toDoItemUIModel.deadLineDateMillis,
                    onToDoItemEntryUIAction = onToDoItemEntryUIAction,
                )

                Divider(modifier = Modifier.fillMaxWidth())

                DeleteTextBlock(
                    isEnabled = toDoItemEntryUIState.toDoItemUIModel.id != null,
                    onToDoItemEntryUIAction = onToDoItemEntryUIAction,
                )
            }
        }
    }
}
