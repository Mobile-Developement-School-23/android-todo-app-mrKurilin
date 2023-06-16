package com.example.todoapp.presentation.to_do_item_entry

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.todoapp.R
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

    private val _currentToDoItemUIModel = MutableStateFlow(
        ToDoItemUIModel(
            text = "",
            priorityStringId = R.string.no,
            deadLineDate = null,
        )
    )
    val uiStateFlow: StateFlow<ToDoItemUIModel> = _currentToDoItemUIModel

    fun onSavePressed(toDoItemId: String?) {
        val toDoItemUIModel = _currentToDoItemUIModel.value
        if (toDoItemId == null) {
            todoItemsRepository.addToDoItem(
                toDoItemUIMapper.toToDoItem(toDoItemUIModel)
            )
        } else {
            val priority = when (toDoItemUIModel.priorityStringId) {
                R.string.no -> {
                    ToDoItemPriority.NORMAL
                }

                R.string.high -> {
                    ToDoItemPriority.HIGH
                }

                R.string.low -> {
                    ToDoItemPriority.LOW
                }

                else -> {
                    throw IllegalStateException()
                }
            }

            todoItemsRepository.updateToDoItem(
                toDoItemId = toDoItemId,
                text = toDoItemUIModel.text,
                deadLineDate = toDoItemUIModel.deadLineDate,
                priority = priority,
            )
        }
    }

    fun loadToDoItem(toDoItemId: String?) {
        if (toDoItemId != null) {
            val toDoItem: ToDoItem = todoItemsRepository.getToDoItem(toDoItemId)
            val toDoItemUIModel = toDoItemUIMapper.toToDoItemUIModel(toDoItem)
            _currentToDoItemUIModel.value = toDoItemUIModel
        }
    }

    fun deleteToDoItem(toDoItemId: String?) {
        if (toDoItemId != null) {
            todoItemsRepository.deleteToDoItem(toDoItemId)
        }
    }

    fun onDeadLineSwitchPressed() {
        val currentToDoItemUIModel = _currentToDoItemUIModel.value
        val deadLineDate = if (currentToDoItemUIModel.deadLineDate == null) {
            Date().time
        } else {
            null
        }
        _currentToDoItemUIModel.value = _currentToDoItemUIModel.value.copy(
            deadLineDate = deadLineDate
        )
    }

    fun onDeadLineDateChanged(dateLong: Long) {
        _currentToDoItemUIModel.value = _currentToDoItemUIModel.value.copy(
            deadLineDate = dateLong
        )
    }

    fun textChanged(text: String) {
        _currentToDoItemUIModel.value = _currentToDoItemUIModel.value.copy(
            text = text
        )
    }

    fun onSpinnerItemSelectedListener(priorityStringId: Int) {
        _currentToDoItemUIModel.value = _currentToDoItemUIModel.value.copy(
            priorityStringId = priorityStringId
        )
    }
}