package com.example.todoapp.presentation.to_do_item_entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.domain.model.ToDoItem
import com.example.todoapp.domain.model.ToDoItemImportance
import com.example.todoapp.domain.usecase.AddToDoItemUseCase
import com.example.todoapp.domain.usecase.DeleteToDoItemByIdUseCase
import com.example.todoapp.domain.usecase.GetToDoItemByIdUseCase
import com.example.todoapp.domain.usecase.UpdateToDoItemUseCase
import com.example.todoapp.presentation.to_do_item_entry.model.ToDoItemUIMapper
import com.example.todoapp.presentation.to_do_item_entry.model.ToDoItemUIModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

class ToDoItemEntryViewModel @Inject constructor(
    private val toDoItemUIMapper: ToDoItemUIMapper,
    private val addToDoItemUseCase: AddToDoItemUseCase,
    private val updateToDoItemUseCase: UpdateToDoItemUseCase,
    private val deleteToDoItemByIdUseCase: DeleteToDoItemByIdUseCase,
    private val getToDoItemByIdUseCase: GetToDoItemByIdUseCase,
) : ViewModel() {

    private val _currentToDoItemUIModelMutableStateFlow = MutableStateFlow(
        ToDoItemUIModel(
            text = "",
            priorityValue = ToDoItemImportance.BASIC.value,
            deadLineDate = null,
        )
    )
    val uiStateFlow: StateFlow<ToDoItemUIModel> = _currentToDoItemUIModelMutableStateFlow

    fun onSavePressed(toDoItemId: String?) = viewModelScope.launch {
        val toDoItemUIModel = _currentToDoItemUIModelMutableStateFlow.value
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

    fun loadToDoItem(toDoItemId: String?) = viewModelScope.launch {
        if (toDoItemId != null) {
            val toDoItem: ToDoItem = getToDoItemByIdUseCase.get(toDoItemId)
            val toDoItemUIModel = toDoItemUIMapper.map(toDoItem)
            _currentToDoItemUIModelMutableStateFlow.value = toDoItemUIModel
        }
    }

    fun deleteToDoItem(toDoItemId: String?) = viewModelScope.launch {
        if (toDoItemId != null) {
            deleteToDoItemByIdUseCase.delete(toDoItemId)
        }
    }

    fun onDeadLineSwitchPressed() {
        val currentToDoItemUIModel = _currentToDoItemUIModelMutableStateFlow.value
        val deadLineDate = if (currentToDoItemUIModel.deadLineDate == null) {
            Date().time
        } else {
            null
        }
        _currentToDoItemUIModelMutableStateFlow.value =
            _currentToDoItemUIModelMutableStateFlow.value.copy(
                deadLineDate = deadLineDate
            )
    }

    fun onDeadLineDateChanged(dateLong: Long) {
        _currentToDoItemUIModelMutableStateFlow.value =
            _currentToDoItemUIModelMutableStateFlow.value.copy(
                deadLineDate = dateLong
            )
    }

    fun textChanged(text: String) {
        _currentToDoItemUIModelMutableStateFlow.value =
            _currentToDoItemUIModelMutableStateFlow.value.copy(
                text = text
            )
    }

    fun onSpinnerItemSelectedListener(priorityStringId: Int) {
        _currentToDoItemUIModelMutableStateFlow.value =
            _currentToDoItemUIModelMutableStateFlow.value.copy(
                priorityValue = priorityStringId
            )
    }
}