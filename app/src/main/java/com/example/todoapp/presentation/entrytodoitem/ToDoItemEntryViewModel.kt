package com.example.todoapp.presentation.entrytodoitem

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.domain.model.ToDoItemImportance
import com.example.todoapp.domain.usecase.AddToDoItemUseCase
import com.example.todoapp.domain.usecase.DeleteToDoItemByIdUseCase
import com.example.todoapp.domain.usecase.GetToDoItemByIdUseCase
import com.example.todoapp.domain.usecase.UpdateToDoItemUseCase
import com.example.todoapp.presentation.entrytodoitem.compose.ToDoItemEntryUIAction
import com.example.todoapp.presentation.entrytodoitem.model.ToDoItemUIMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * Managing the UI state and business logic related to creating and editing ToDoItems in the
 * presentation layer of the application.
 */
class ToDoItemEntryViewModel @Inject constructor(
    private val toDoItemUIMapper: ToDoItemUIMapper,
    private val addToDoItemUseCase: AddToDoItemUseCase,
    private val updateToDoItemUseCase: UpdateToDoItemUseCase,
    private val deleteToDoItemByIdUseCase: DeleteToDoItemByIdUseCase,
    private val getToDoItemByIdUseCase: GetToDoItemByIdUseCase,
) : ViewModel() {

    var uiState by mutableStateOf(ToDoItemEntryUIState())
        private set

    private val _canBeClosed = MutableStateFlow(false)
    val canBeClosed = _canBeClosed.asStateFlow()

    private fun onSavePressed() = viewModelScope.launch {
        val toDoItemUIModel = uiState.toDoItemUIModel

        uiState = uiState.copy(isLoading = true)

        if (toDoItemUIModel.id == null) {
            addToDoItemUseCase.add(toDoItemUIMapper.map(toDoItemUIModel))
        } else {
            updateToDoItemUseCase.update(
                toDoItemId = toDoItemUIModel.id,
                text = toDoItemUIModel.text,
                deadLineDate = toDoItemUIModel.deadLineDate,
                importance = ToDoItemImportance.fromValue(toDoItemUIModel.priorityStringId),
            )
        }

        _canBeClosed.value = true
    }

    fun loadToDoItem(toDoItemId: String?) = viewModelScope.launch {
        uiState = uiState.copy(isLoading = true)
        if (toDoItemId != null) {
            val toDoItem = getToDoItemByIdUseCase.get(toDoItemId)
            val toDoItemUIModel = toDoItemUIMapper.map(toDoItem)
            uiState = uiState.copy(toDoItemUIModel = toDoItemUIModel)
        }
        uiState = uiState.copy(isLoading = false)
    }

    private fun deleteToDoItem() = viewModelScope.launch {
        val id = uiState.toDoItemUIModel.id ?: return@launch

        uiState = uiState.copy(isLoading = true)

        deleteToDoItemByIdUseCase.delete(id)
        _canBeClosed.value = true
    }

    private fun onDeadLineSwitchPressed() {
        val deadLineDate = if (uiState.toDoItemUIModel.deadLineDate == null) {
            Date().time
        } else {
            null
        }
        val updatedToDoItemUIModel = uiState.toDoItemUIModel.copy(deadLineDate = deadLineDate)
        uiState = uiState.copy(toDoItemUIModel = updatedToDoItemUIModel)
    }

    private fun textChanged(text: String) {
        if (text != uiState.toDoItemUIModel.text) {
            val updatedToDoItemUIModel = uiState.toDoItemUIModel.copy(text = text)
            uiState = uiState.copy(toDoItemUIModel = updatedToDoItemUIModel)
        }
    }

    fun onSpinnerItemSelected(priorityStringId: Int) {
        if (uiState.toDoItemUIModel.priorityStringId != priorityStringId) {
            val updatedToDoItemUIModel = uiState.toDoItemUIModel.copy(
                priorityStringId = priorityStringId
            )
            uiState = uiState.copy(toDoItemUIModel = updatedToDoItemUIModel)
        }
    }

    fun onToDoItemEntryUIAction(toDoItemEntryUIAction: ToDoItemEntryUIAction) {
        when (toDoItemEntryUIAction) {
            ToDoItemEntryUIAction.CloseButtonPressed -> {
                _canBeClosed.value = true
            }

            ToDoItemEntryUIAction.DeadLineSwitchStateChanged -> {
                onDeadLineSwitchPressed()
            }

            ToDoItemEntryUIAction.SaveButtonPressed -> {
                onSavePressed()
            }

            is ToDoItemEntryUIAction.SelectedDateChanged -> {
                val date = toDoItemEntryUIAction.deadLineDate
                val updatedToDoItemUIModel = uiState.toDoItemUIModel.copy(deadLineDate = date)
                uiState = uiState.copy(toDoItemUIModel = updatedToDoItemUIModel)
            }

            is ToDoItemEntryUIAction.TextChanged -> {
                textChanged(toDoItemEntryUIAction.text)
            }

            is ToDoItemEntryUIAction.PrioritySelected -> {
                val updatedToDoItemUIModel =
                    uiState.toDoItemUIModel.copy(priorityStringId = toDoItemEntryUIAction.priorityStringId)
                uiState = uiState.copy(toDoItemUIModel = updatedToDoItemUIModel)
            }

            ToDoItemEntryUIAction.DeleteButtonPressed -> {
                deleteToDoItem()
            }
        }
    }
}