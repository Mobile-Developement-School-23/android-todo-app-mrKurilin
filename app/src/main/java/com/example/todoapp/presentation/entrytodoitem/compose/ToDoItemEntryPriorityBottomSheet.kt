import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.presentation.entrytodoitem.compose.ToDoItemEntryUIAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoItemEntryPriorityBottomSheet(
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = false,
            initialValue = SheetValue.Hidden
        )
    ),
    onToDoItemEntryUIAction: (ToDoItemEntryUIAction) -> Unit = {},
    scope: CoroutineScope = rememberCoroutineScope(),
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.low),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .height(48.dp)
                .clickable {
                    onToDoItemEntryUIAction(ToDoItemEntryUIAction.PrioritySelected(R.string.low))
                    scope.launch {
                        scaffoldState.bottomSheetState.hide()
                    }
                },

            )
        Text(
            text = stringResource(id = R.string.basic),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .height(48.dp)
                .clickable {
                    onToDoItemEntryUIAction(ToDoItemEntryUIAction.PrioritySelected(R.string.basic))
                    scope.launch {
                        scaffoldState.bottomSheetState.hide()
                    }
                }
        )
        Text(
            text = stringResource(id = R.string.high_importance),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .height(48.dp)
                .clickable {
                    onToDoItemEntryUIAction(ToDoItemEntryUIAction.PrioritySelected(R.string.high_importance))
                    scope.launch {
                        scaffoldState.bottomSheetState.hide()
                    }
                }
        )

    }
}
