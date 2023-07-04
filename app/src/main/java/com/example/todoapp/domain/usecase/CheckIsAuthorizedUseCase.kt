package com.example.todoapp.domain.usecase

import android.content.SharedPreferences
import com.example.todoapp.data.local.SharedPreferencesConst
import javax.inject.Inject

class CheckIsAuthorizedUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun isAuthorized(): Boolean {
        return sharedPreferences.getString(
            SharedPreferencesConst.TOKEN_KEY,
            SharedPreferencesConst.NOT_AUTHORIZED
        ) != SharedPreferencesConst.NOT_AUTHORIZED
    }
}