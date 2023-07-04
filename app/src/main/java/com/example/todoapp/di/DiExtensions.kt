package com.example.todoapp.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.todoapp.ToDoApp

fun Fragment.appComponent(): AppComponent {
    return (requireActivity().application as ToDoApp).appComponent
}

inline fun <reified T : ViewModel> Fragment.lazyViewModel(
    noinline create: () -> T,
): Lazy<T> {
    return viewModels {
        ViewModelFactory(this, create)
    }
}