package com.example.todoapp.presentation.util

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
}

fun Fragment.showLongToast(@StringRes id: Int) {
    Toast.makeText(requireContext(), getText(id), Toast.LENGTH_LONG).show()
}