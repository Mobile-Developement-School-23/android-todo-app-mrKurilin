package com.example.todoapp.presentation.to_do_item_entry

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.todoapp.ToDoApp
import com.example.todoapp.domain.model.ToDoItem
import com.example.todoapp.domain.model.ToDoItemPriority
import com.example.todoapp.presentation.to_do_item_entry.model.ToDoItemUIModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date

class ToDoItemEntryViewModel(app: Application) : AndroidViewModel(app) {

    private val toDoApp = app as ToDoApp

    private val todoItemsRepository = toDoApp.provideTodoItemsRepository()
    private val toDoItemUIMapper = toDoApp.provideToDoItemUIMapper()

    private val _currentToDoItemUIModelMutableStateFlow = MutableStateFlow(
        ToDoItemUIModel(
            text = "",
            priorityValue = ToDoItemPriority.NORMAL.value,
            deadLineDate = null,
        )
    )
    val uiStateFlow: StateFlow<ToDoItemUIModel> = _currentToDoItemUIModelMutableStateFlow

    fun onSavePressed(toDoItemId: String?) {
        val toDoItemUIModel = _currentToDoItemUIModelMutableStateFlow.value
        if (toDoItemId == null) {
            todoItemsRepository.addToDoItem(
                toDoItemUIMapper.toToDoItem(toDoItemUIModel)
            )
        } else {
            todoItemsRepository.updateToDoItem(
                toDoItemId = toDoItemId,
                text = toDoItemUIModel.text,
                deadLineDate = toDoItemUIModel.deadLineDate,
                priority = ToDoItemPriority.from(toDoItemUIModel.priorityValue),
            )
        }
    }

    fun loadToDoItem(toDoItemId: String?) {
        if (toDoItemId != null) {
            val toDoItem: ToDoItem = todoItemsRepository.getToDoItem(toDoItemId)
            val toDoItemUIModel = toDoItemUIMapper.toToDoItemUIModel(toDoItem)
            _currentToDoItemUIModelMutableStateFlow.value = toDoItemUIModel
        }
    }

    fun deleteToDoItem(toDoItemId: String?) {
        if (toDoItemId != null) {
            todoItemsRepository.deleteToDoItem(toDoItemId)
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