package com.example.todoapp.presentation.entrytodoitem.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.todoapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityColumn(
    @StringRes priorityStringId: Int,
    scaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                scope.launch {
                    scaffoldState.bottomSheetState.expand()
                }
            }
    ) {
        Text(
            text = stringResource(id = R.string.priority)
        )
        Text(
            text = stringResource(id = priorityStringId),
            color = if (priorityStringId == R.string.high_importance) {
                Color.Red
            } else {
                Color.Black
            }
        )
    }
}