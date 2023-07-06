package com.example.todoapp.presentation.entrytodoitem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.domain.model.ToDoItemImportance
import com.example.todoapp.domain.usecase.AddToDoItemUseCase
import com.example.todoapp.domain.usecase.DeleteToDoItemByIdUseCase
import com.example.todoapp.domain.usecase.GetToDoItemByIdUseCase
import com.example.todoapp.domain.usecase.UpdateToDoItemUseCase
import com.example.todoapp.presentation.entrytodoitem.model.ToDoItemUIMapper
import com.example.todoapp.presentation.entrytodoitem.model.ToDoItemUIModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

    private var toDoItemUIModelMutableStateFlow = MutableStateFlow(
        ToDoItemUIModel(
            text = "",
            priorityValue = ToDoItemImportance.BASIC.value,
            deadLineDate = null,
        )
    )

    init {
        viewModelScope.launch {
            toDoItemUIModelMutableStateFlow.collect { toDoItemUIModel ->
                _toDoItemEntryUIStateMutableStateFlow.update {
                    ToDoItemEntryUIState.ToDoItemUIModelUpdated(toDoItemUIModel)
                }
            }
        }
    }

    fun onSavePressed(toDoItemId: String?) {
        _toDoItemEntryUIStateMutableStateFlow.update {
            ToDoItemEntryUIState.Loading
        }

        val toDoItemUIModel = toDoItemUIModelMutableStateFlow.value

        viewModelScope.launch {
            runBlocking {
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
            }

            _toDoItemEntryUIStateMutableStateFlow.update {
                ToDoItemEntryUIState.Closing
            }
        }
    }

    fun loadToDoItem(toDoItemId: String?) = viewModelScope.launch {
        if (toDoItemId != null) {
            val toDoItem = getToDoItemByIdUseCase.get(toDoItemId)
            toDoItemUIModelMutableStateFlow.update {
                toDoItemUIMapper.map(toDoItem)
            }
        }
    }

    fun deleteToDoItem(toDoItemId: String?) {
        if (toDoItemId == null) {
            return
        }

        _toDoItemEntryUIStateMutableStateFlow.update {
            ToDoItemEntryUIState.Loading
        }

        viewModelScope.launch {
            runBlocking {
                deleteToDoItemByIdUseCase.delete(toDoItemId)
            }
            _toDoItemEntryUIStateMutableStateFlow.update {
                ToDoItemEntryUIState.Closing
            }
        }
    }

    fun onDeadLineSwitchPressed() {
        val toDoItemUIModel = toDoItemUIModelMutableStateFlow.value

        val deadLineDate = if (toDoItemUIModel.deadLineDate == null) {
            Date().time
        } else {
            null
        }
        toDoItemUIModelMutableStateFlow.update {
            toDoItemUIModel.copy(
                deadLineDate = deadLineDate
            )
        }
    }

    fun onDeadLineDateChanged(dateLong: Long) {
        val toDoItemUIModel = toDoItemUIModelMutableStateFlow.value
        toDoItemUIModelMutableStateFlow.update {
            toDoItemUIModel.copy(
                deadLineDate = dateLong
            )
        }
    }

    fun textChanged(text: String) {
        val toDoItemUIModel = toDoItemUIModelMutableStateFlow.value
        toDoItemUIModelMutableStateFlow.update {
            toDoItemUIModel.copy(
                text = text
            )
        }
    }

    fun onSpinnerItemSelectedListener(priorityStringId: Int) {
        val toDoItemUIModel = toDoItemUIModelMutableStateFlow.value
        toDoItemUIModelMutableStateFlow.update {
            toDoItemUIModel.copy(
                priorityValue = priorityStringId
            )
        }
    }
}