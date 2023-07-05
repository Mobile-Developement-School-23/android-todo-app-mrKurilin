package com.example.todoapp.presentation.todolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.domain.usecase.CheckIsAuthorizedUseCase
import com.example.todoapp.domain.usecase.DeleteToDoItemByIdUseCase
import com.example.todoapp.domain.usecase.GetToDoItemListFlowUseCase
import com.example.todoapp.domain.usecase.LogOutUseCase
import com.example.todoapp.domain.usecase.SetDoneToDoItemUseCase
import com.example.todoapp.domain.usecase.UpdateDataUseCase
import com.example.todoapp.presentation.Notification
import com.example.todoapp.presentation.todolist.model.ToDoListItemUIMapper
import com.example.todoapp.presentation.todolist.model.ToDoListItemUIModel
import com.example.todoapp.presentation.util.ConnectivityStateObserver
import com.example.todoapp.presentation.util.NetworkConnectivityState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The [ToDoListViewModel] class is responsible for managing the state and business
 * logic of the ToDoList feature in the application.
 */
class ToDoListViewModel @Inject constructor(
    private val toDoListItemUIMapper: ToDoListItemUIMapper,
    private val getToDoItemListFlowUseCase: GetToDoItemListFlowUseCase,
    private val deleteToDoItemByIdUseCase: DeleteToDoItemByIdUseCase,
    private val setDoneToDoItemUseCase: SetDoneToDoItemUseCase,
    private val updateDataUseCase: UpdateDataUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val connectivityStateObserver: ConnectivityStateObserver,
    checkIsAuthorizedUseCase: CheckIsAuthorizedUseCase,
) : ViewModel() {

    private val _toDoListUIStateMutableStateFlow = MutableStateFlow(
        ToDoListUIState(isAuthorized = checkIsAuthorizedUseCase.isAuthorized())
    )
    val toDoListUIStateStateFlow = _toDoListUIStateMutableStateFlow.asStateFlow()

    init {
        observeConnectivityState()
    }

    fun deleteToDoItem(toDoItemId: String) = viewModelScope.launch {
        deleteToDoItemByIdUseCase.delete(toDoItemId)
    }

    fun setDoneToDoItem(toDoItemId: String) = viewModelScope.launch {
        setDoneToDoItemUseCase.set(toDoItemId)
    }

    fun changeDoneItemsVisibility() {
        val currentToDoListUIState = _toDoListUIStateMutableStateFlow.value
        val newToDoListUIState = currentToDoListUIState.copy(
            isDoneItemsVisible = !currentToDoListUIState.isDoneItemsVisible
        )
        _toDoListUIStateMutableStateFlow.update {
            newToDoListUIState
        }
    }

    fun notificationShown() {
        val currentToDoListUIState = _toDoListUIStateMutableStateFlow.value
        val newToDoListUIState = currentToDoListUIState.copy(
            notification = null
        )
        _toDoListUIStateMutableStateFlow.update {
            newToDoListUIState
        }
    }

    suspend fun getToDoItemListFlow(): Flow<List<ToDoListItemUIModel>> {
        return getToDoItemListFlowUseCase.get().map { list ->
            updateDoneItemsCount(list.count { it.isDone })
            list.map { toDoItem -> toDoListItemUIMapper.map(toDoItem) }
        }.combine(_toDoListUIStateMutableStateFlow) { list, toDoListUIState ->
            if (!toDoListUIState.isDoneItemsVisible) list.filter { !it.isDone } else list
        }
    }

    private fun updateDoneItemsCount(count: Int) {
        val currentToDoListUIState = _toDoListUIStateMutableStateFlow.value
        val newToDoListUIState = currentToDoListUIState.copy(
            doneToDoItemsCount = count
        )
        _toDoListUIStateMutableStateFlow.update {
            newToDoListUIState
        }
    }

    suspend fun updateData() = viewModelScope.launch {
        var currentToDoListUIState = _toDoListUIStateMutableStateFlow.value
        _toDoListUIStateMutableStateFlow.update {
            currentToDoListUIState.copy(isUpdatingData = true)
        }

        val notification = if (updateDataUseCase.update().isSuccess) {
            Notification.DATA_SYNCHRONIZED
        } else {
            Notification.SYNCHRONIZATION_ERROR
        }
        currentToDoListUIState = _toDoListUIStateMutableStateFlow.value
        val newToDoListUIState = currentToDoListUIState.copy(
            isUpdatingData = false,
            notification = notification
        )
        _toDoListUIStateMutableStateFlow.update { newToDoListUIState }
    }

    fun logOut() {
        logOutUseCase.logOut()
    }

    private fun observeConnectivityState() = viewModelScope.launch {
        connectivityStateObserver.networkConnectivityState.collect { state ->
            handleNetworkConnectivityState(state)
        }
    }

    private fun handleNetworkConnectivityState(state: NetworkConnectivityState) {
        val currentToDoListUIState = _toDoListUIStateMutableStateFlow.value

        val isNoInternetConnection = state == NetworkConnectivityState.LOST

        _toDoListUIStateMutableStateFlow.update {
            currentToDoListUIState.copy(isNoInternetConnection = isNoInternetConnection)
        }
    }
}