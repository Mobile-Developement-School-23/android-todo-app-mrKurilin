package com.example.todoapp.presentation.to_do_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.domain.usecase.DeleteToDoItemByIdUseCase
import com.example.todoapp.domain.usecase.GetToDoItemListFlowUseCase
import com.example.todoapp.domain.usecase.SetDoneToDoItemUseCase
import com.example.todoapp.domain.usecase.UpdateDataUseCase
import com.example.todoapp.presentation.to_do_list.model.ToDoListItemUIMapper
import com.example.todoapp.presentation.to_do_list.model.ToDoListItemUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ToDoListViewModel @Inject constructor(
    private val toDoListItemUIMapper: ToDoListItemUIMapper,
    private val getToDoItemListFlowUseCase: GetToDoItemListFlowUseCase,
    private val deleteToDoItemByIdUseCase: DeleteToDoItemByIdUseCase,
    private val setDoneToDoItemUseCase: SetDoneToDoItemUseCase,
    private val updateDataUseCase: UpdateDataUseCase,
) : ViewModel() {

    private val _isDoneItemsVisibleStateFlow = MutableStateFlow(true)

    private val _textToShowFlow = MutableStateFlow<String?>(null)
    val textToShowFlow: StateFlow<String?> = _textToShowFlow

    init {
        viewModelScope.launch {
            updateDataUseCase.update()
        }
    }

    fun deleteToDoItem(toDoItemId: String) = viewModelScope.launch {
        deleteToDoItemByIdUseCase.delete(toDoItemId)
    }

    fun setDoneToDoItem(toDoItemId: String) = viewModelScope.launch {
        setDoneToDoItemUseCase.set(toDoItemId)
    }

    fun changeDoneItemsVisibility() {
        _isDoneItemsVisibleStateFlow.value = !_isDoneItemsVisibleStateFlow.value
    }

    fun toastShown() {
        _textToShowFlow.update {
            null
        }
    }

    suspend fun getToDoItemListFlow(): Flow<List<ToDoListItemUIModel>> {
        return getToDoItemListFlowUseCase.get().map { list ->
            list.map { toDoItem ->
                toDoListItemUIMapper.map(toDoItem)
            }
        }.combine(_isDoneItemsVisibleStateFlow) { list, isVisible ->
            if (!isVisible) {
                list.filter { !it.isDone }
            } else {
                list
            }
        }
    }
}