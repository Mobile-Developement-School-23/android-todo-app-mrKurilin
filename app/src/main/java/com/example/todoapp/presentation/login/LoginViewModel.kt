package com.example.todoapp.presentation.login

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.todoapp.data.local.TOKEN_KEY
import com.example.todoapp.domain.usecase.CheckIsAuthorizedUseCase
import javax.inject.Inject

/**
 * Managing the login-related functionality in the presentation layer of the application.
 */
class LoginViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val checkIsAuthorizedUseCase: CheckIsAuthorizedUseCase,
) : ViewModel() {

    fun putToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    fun isAuthorized(): Boolean {
        return checkIsAuthorizedUseCase.isAuthorized()
    }
}