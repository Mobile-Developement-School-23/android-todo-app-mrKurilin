package com.example.todoapp.presentation.to_do_list_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.domain.usecase.CheckIsAuthorizedUseCase
import com.example.todoapp.domain.usecase.DeleteToDoItemByIdUseCase
import com.example.todoapp.domain.usecase.GetToDoItemListFlowUseCase
import com.example.todoapp.domain.usecase.LogOutUseCase
import com.example.todoapp.domain.usecase.SetDoneToDoItemUseCase
import com.example.todoapp.domain.usecase.UpdateDataUseCase
import com.example.todoapp.presentation.Notification
import com.example.todoapp.presentation.to_do_list_fragment.model.ToDoListItemUIMapper
import com.example.todoapp.presentation.to_do_list_fragment.model.ToDoListItemUIModel
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

class ToDoListViewModel @Inject constructor(
    private val toDoListItemUIMapper: ToDoListItemUIMapper,
    private val getToDoItemListFlowUseCase: GetToDoItemListFlowUseCase,
    private val deleteToDoItemByIdUseCase: DeleteToDoItemByIdUseCase,
    private val setDoneToDoItemUseCase: SetDoneToDoItemUseCase,
    private val updateDataUseCase: UpdateDataUseCase,
    checkIsAuthorizedUseCase: CheckIsAuthorizedUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val connectivityStateObserver: ConnectivityStateObserver
) : ViewModel() {

    private val _toDoListUIStateMutableStateFlow = MutableStateFlow(
        ToDoListUIState(
            isAuthorized = checkIsAuthorizedUseCase.isAuthorized()
        )
    )
    val toDoListUIStateStateFlow = _toDoListUIStateMutableStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            connectivityStateObserver.networkConnectivityState.collect { state ->
                handleNetworkConnectivityState(state)
            }
        }
    }

    fun deleteToDoItem(toDoItemId: String) = viewModelScope.launch {
        deleteToDoItemByIdUseCase.delete(toDoItemId)
    }

    fun setDoneToDoItem(toDoItemId: String) = viewModelScope.launch {
        setDoneToDoItemUseCase.set(toDoItemId)
    }

    fun changeDoneItemsVisibility() {
        val currentToDoListUIState = _toDoListUIStateMutableStateFlow.value
        _toDoListUIStateMutableStateFlow.update {
            currentToDoListUIState.copy(
                isDoneItemsVisible = !currentToDoListUIState.isDoneItemsVisible
            )
        }
    }

    fun notifyShown() {
        val currentToDoListUIState = _toDoListUIStateMutableStateFlow.value
        _toDoListUIStateMutableStateFlow.update {
            currentToDoListUIState.copy(
                notification = null
            )
        }
    }

    suspend fun getToDoItemListFlow(): Flow<List<ToDoListItemUIModel>> {
        return getToDoItemListFlowUseCase.get().map { list ->
            updateDoneItemsCount(list.count { it.isDone })
            list.map { toDoItem ->
                toDoListItemUIMapper.map(toDoItem)
            }
        }.combine(_toDoListUIStateMutableStateFlow) { list, toDoListUIState ->
            if (!toDoListUIState.isDoneItemsVisible) {
                list.filter { !it.isDone }
            } else {
                list
            }
        }
    }

    private fun updateDoneItemsCount(count: Int) {
        val currentToDoListUIState = _toDoListUIStateMutableStateFlow.value
        _toDoListUIStateMutableStateFlow.update {
            currentToDoListUIState.copy(
                doneToDoItemsCount = count
            )
        }
    }

    suspend fun updateData() = viewModelScope.launch {
        var currentToDoListUIState = _toDoListUIStateMutableStateFlow.value
        _toDoListUIStateMutableStateFlow.update {
            currentToDoListUIState.copy(
                isUpdatingData = true
            )
        }

        val result = updateDataUseCase.update()

        if (result.isSuccess) {
            currentToDoListUIState = _toDoListUIStateMutableStateFlow.value
            _toDoListUIStateMutableStateFlow.update {
                currentToDoListUIState.copy(
                    isUpdatingData = false,
                    notification = Notification.DATA_SYNCHRONIZED
                )
            }
        } else {
            _toDoListUIStateMutableStateFlow.update {
                currentToDoListUIState.copy(
                    isUpdatingData = false,
                    notification = Notification.SYNCHRONIZATION_ERROR
                )
            }
        }

    }

    private fun handleNetworkConnectivityState(state: NetworkConnectivityState) {
        val currentToDoListUIState = _toDoListUIStateMutableStateFlow.value

        val isNoInternetConnection = when (state) {
            NetworkConnectivityState.AVAILABLE -> {
                false
            }

            NetworkConnectivityState.LOST -> {
                true
            }
        }

        _toDoListUIStateMutableStateFlow.update {
            currentToDoListUIState.copy(
                isNoInternetConnection = isNoInternetConnection
            )
        }
    }

    fun logOut() {
        logOutUseCase.logOut()
    }
}