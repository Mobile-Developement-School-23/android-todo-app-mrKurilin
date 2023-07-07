package com.example.todoapp.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.todoapp.ToDoApp
import com.example.todoapp.di.component.AppComponent
import com.example.todoapp.di.component.DataWorkComponent

fun Fragment.appComponent(): AppComponent {
    return (requireActivity().application as ToDoApp).provideAppComponent()
}

fun Fragment.dataWorkComponent(): DataWorkComponent {
    return (requireActivity().application as ToDoApp).provideAppComponent().dataWorkComponent()
}

inline fun <reified T : ViewModel> Fragment.lazyViewModel(
    noinline create: () -> T,
): Lazy<T> {
    return viewModels {
        ViewModelFactory(this, create)
    }
}