package com.example.todoapp.di

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner

class ViewModelFactory<T : ViewModel>(
    savedStateRegistryOwner: SavedStateRegistryOwner,
    private val create: () -> T,
) : AbstractSavedStateViewModelFactory(savedStateRegistryOwner, null) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ): T {
        @Suppress("UNCHECKED_CAST")
        return create.invoke() as T
    }
}