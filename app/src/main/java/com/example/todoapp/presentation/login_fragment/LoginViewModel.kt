package com.example.todoapp.presentation.login_fragment

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.todoapp.data.local.SharedPreferencesConst.Companion.TOKEN_KEY
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {

    fun putToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }
}