package com.example.todoapp.presentation.entrytodoitem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.domain.model.ToDoItemImportance
import com.example.todoapp.domain.usecase.AddToDoItemUseCase
import com.example.todoapp.domain.usecase.DeleteToDoItemByIdUseCase
import com.example.todoapp.domain.usecase.GetToDoItemByIdUseCase
import com.example.todoapp.domain.usecase.UpdateToDoItemUseCase
import com.example.todoapp.presentation.entrytodoitem.model.ToDoItemUIMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    private val _toDoItemEntryUIStateMutableStateFlow = MutableStateFlow<ToDoItemEntryUIState>(
        ToDoItemEntryUIState.Loading
    )
    val toDoItemEntryUIStateMutableStateFlow = _toDoItemEntryUIStateMutableStateFlow.asStateFlow()

    fun onSavePressed(toDoItemId: String?) = viewModelScope.launch {
        val toDoItemUIModel = _toDoItemEntryUIStateMutableStateFlow.value.toDoItemUIModel

        _toDoItemEntryUIStateMutableStateFlow.update {
            ToDoItemEntryUIState.Loading
        }

        if (toDoItemId == null) {
            addToDoItemUseCase.add(toDoItemUIMapper.map(toDoItemUIModel))
        } else {
            updateToDoItemUseCase.update(
                toDoItemId = toDoItemId,
                text = toDoItemUIModel.text,
                deadLineDate = toDoItemUIModel.deadLineDate,
                importance = ToDoItemImportance.from(toDoItemUIModel.priorityValue),
            )
        }

        _toDoItemEntryUIStateMutableStateFlow.update {
            ToDoItemEntryUIState.Closing
        }
    }

    fun loadToDoItem(toDoItemId: String?) = viewModelScope.launch {
        if (toDoItemId != null) {
            val toDoItem = getToDoItemByIdUseCase.get(toDoItemId)
            val toDoItemUIModel = toDoItemUIMapper.map(toDoItem)
            _toDoItemEntryUIStateMutableStateFlow.update {
                ToDoItemEntryUIState.ToDoItemUIModelUpdated(toDoItemUIModel)
            }
        } else {
            _toDoItemEntryUIStateMutableStateFlow.update {
                ToDoItemEntryUIState.ShowInit
            }
        }
    }

    fun deleteToDoItem(toDoItemId: String?) = viewModelScope.launch {
        if (toDoItemId == null) {
            return@launch
        }

        _toDoItemEntryUIStateMutableStateFlow.update {
            ToDoItemEntryUIState.Loading
        }

        deleteToDoItemByIdUseCase.delete(toDoItemId)
        _toDoItemEntryUIStateMutableStateFlow.update {
            ToDoItemEntryUIState.Closing
        }
    }

    fun onDeadLineSwitchPressed() {
        val toDoItemUIModel = _toDoItemEntryUIStateMutableStateFlow.value.toDoItemUIModel

        val deadLineDate = if (toDoItemUIModel.deadLineDate == null) {
            Date().time
        } else {
            null
        }
        val updatedToDoItemUIModel = toDoItemUIModel.copy(
            deadLineDate = deadLineDate
        )
        _toDoItemEntryUIStateMutableStateFlow.update {
            ToDoItemEntryUIState.ToDoItemUIModelUpdated(updatedToDoItemUIModel)
        }
    }

    fun onDeadLineDateChanged(dateLong: Long) {
        val toDoItemUIModel = _toDoItemEntryUIStateMutableStateFlow.value.toDoItemUIModel
        val updatedToDoItemUIModel = toDoItemUIModel.copy(
            deadLineDate = dateLong
        )
        _toDoItemEntryUIStateMutableStateFlow.update {
            ToDoItemEntryUIState.ToDoItemUIModelUpdated(updatedToDoItemUIModel)
        }
    }

    fun textChanged(text: String) {
        if (text == _toDoItemEntryUIStateMutableStateFlow.value.toDoItemUIModel.text) {
            return
        }
        val toDoItemUIModel = _toDoItemEntryUIStateMutableStateFlow.value.toDoItemUIModel
        val updatedToDoItemUIModel = toDoItemUIModel.copy(
            text = text
        )
        _toDoItemEntryUIStateMutableStateFlow.update {
            ToDoItemEntryUIState.ToDoItemUIModelUpdated(updatedToDoItemUIModel)
        }
    }

    fun onSpinnerItemSelectedListener(priorityStringId: Int) {
        if (_toDoItemEntryUIStateMutableStateFlow.value.toDoItemUIModel.priorityValue == priorityStringId) {
            return
        }
        val toDoItemUIModel = _toDoItemEntryUIStateMutableStateFlow.value.toDoItemUIModel
        val updatedToDoItemUIModel = toDoItemUIModel.copy(
            priorityValue = priorityStringId
        )
        _toDoItemEntryUIStateMutableStateFlow.update {
            ToDoItemEntryUIState.ToDoItemUIModelUpdated(updatedToDoItemUIModel)
        }
    }
}