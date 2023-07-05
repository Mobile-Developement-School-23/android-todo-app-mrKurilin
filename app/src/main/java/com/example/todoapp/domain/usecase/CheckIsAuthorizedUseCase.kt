package com.example.todoapp.domain.usecase

import android.content.SharedPreferences
import com.example.todoapp.data.local.NOT_AUTHORIZED
import com.example.todoapp.data.local.TOKEN_KEY
import javax.inject.Inject

class CheckIsAuthorizedUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun isAuthorized(): Boolean {
        return sharedPreferences.getString(TOKEN_KEY, NOT_AUTHORIZED) != NOT_AUTHORIZED
    }
}