package com.example.todoapp.presentation.to_do_item_entry

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.todoapp.ToDoApp
import com.example.todoapp.domain.model.ToDoItem
import com.example.todoapp.domain.model.ToDoItemPriority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ToDoItemEntryViewModel(app: Application) : AndroidViewModel(app) {

    private val toDoApp = app as ToDoApp

    private val todoItemsRepository = toDoApp.provideTodoItemsRepository()
    private val toDoItemUIMapper = toDoApp.provideToDoItemUIMapper()

    private val _uiState = MutableStateFlow(ToDoEntryUIState())
    val uiStateFlow: StateFlow<ToDoEntryUIState> = _uiState

    private val _selectedDateLongFlow = MutableStateFlow(Date().time)
    val selectedDateStringFlow = _selectedDateLongFlow.map { dateLong ->
        SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date(dateLong))
    }

    private var loadedToDoItem: ToDoItem? = null

    fun onSavePressed(
        text: String,
        priority: ToDoItemPriority,
        deadLineDate: Date?,
    ) {
        val loadedToDoItem = loadedToDoItem
        val currentDate = Date()
        if (loadedToDoItem == null) {
            val toDoItem = ToDoItem(
                id = currentDate.time.toString(),
                text = text,
                priority = priority,
                creationDate = currentDate,
                isDone = false,
                deadLineDate = deadLineDate,
            )
            todoItemsRepository.addToDoItem(toDoItem)
        } else {
            val toDoItem = loadedToDoItem.copy(
                text = text,
                priority = priority,
                deadLineDate = deadLineDate,
                editDate = currentDate
            )
            todoItemsRepository.updateToDoItem(toDoItem)
        }
    }

    fun loadToDoItem(toDoItemId: String?) {
        if (toDoItemId != null) {
            val toDoItem: ToDoItem = todoItemsRepository.getToDoItem(toDoItemId)
            loadedToDoItem = toDoItem
            val toDoItemUIModel = toDoItemUIMapper.toToDoItemUIModel(toDoItem)
            _uiState.value = _uiState.value.copy(
                ableToSave = true,
                enabledDeadLine = toDoItemUIModel.deadLineDate != null,
                toDoItemUIModel = toDoItemUIModel,
            )
        }
    }

    fun deleteToDoItem() {
        val loadedToDoItem = this.loadedToDoItem
        if (loadedToDoItem != null) {
            todoItemsRepository.deleteToDoItem(loadedToDoItem.id)
        }
    }

    fun onSwitchPressed() {
        val isEnabledDeadline = _uiState.value.enabledDeadLine
        _uiState.value = _uiState.value.copy(enabledDeadLine = !isEnabledDeadline)
    }

    fun onSelectedDateChanged(dateLong: Long) {
        _selectedDateLongFlow.value = dateLong
    }
}