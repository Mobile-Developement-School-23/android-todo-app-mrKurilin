package com.example.todoapp.domain.usecase

import android.content.SharedPreferences
import com.example.todoapp.data.local.NOT_AUTHORIZED
import com.example.todoapp.data.local.TOKEN_KEY
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun logOut() {
        sharedPreferences.edit().putString(TOKEN_KEY, NOT_AUTHORIZED).apply()
    }
}