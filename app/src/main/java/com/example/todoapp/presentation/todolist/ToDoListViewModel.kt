package com.example.todoapp.presentation.todolist

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.local.NOT_AUTHORIZED
import com.example.todoapp.data.local.TOKEN_KEY
import com.example.todoapp.domain.usecase.DeleteToDoItemByIdUseCase
import com.example.todoapp.domain.usecase.GetToDoItemListFlowUseCase
import com.example.todoapp.domain.usecase.LogOutUseCase
import com.example.todoapp.domain.usecase.SetDoneToDoItemUseCase
import com.example.todoapp.domain.usecase.UpdateDataUseCase
import com.example.todoapp.presentation.Notification
import com.example.todoapp.presentation.todolist.model.ToDoListItemUIMapper
import com.example.todoapp.presentation.util.ConnectivityStateObserver
import com.example.todoapp.presentation.util.NetworkConnectivityState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {

    private val _toDoListUIStateMutableStateFlow = MutableStateFlow(
        ToDoListUIState(isAuthorized = isAuthorized())
    )
    val toDoListUIStateStateFlow = _toDoListUIStateMutableStateFlow.asStateFlow()

    init {
        observeConnectivityState()
        observeToDoItemList()
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

    private fun observeToDoItemList() = viewModelScope.launch {
        _toDoListUIStateMutableStateFlow.combine(
            getToDoItemListFlowUseCase.get()
        ) { state, list ->
            val toDoListItemUIList = list.map { toDoItem -> toDoListItemUIMapper.map(toDoItem) }
            if (!state.isDoneItemsVisible) {
                toDoListItemUIList.filter { !it.isDone }
            } else {
                toDoListItemUIList
            }
        }.collect { toDoListItemUIList ->
            val doneCount = toDoListItemUIList.count { it.isDone }
            val currentToDoListUIState = _toDoListUIStateMutableStateFlow.value
            val updatedToDoListUIState = currentToDoListUIState.copy(
                doneToDoItemsCount = doneCount,
                toDoListItemUIModelList = toDoListItemUIList
            )
            _toDoListUIStateMutableStateFlow.update { updatedToDoListUIState }
        }
    }

    private fun isAuthorized(): Boolean {
        return sharedPreferences.getString(TOKEN_KEY, NOT_AUTHORIZED) != NOT_AUTHORIZED
    }
}