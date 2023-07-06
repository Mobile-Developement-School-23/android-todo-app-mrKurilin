package com.example.todoapp.presentation.login

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthSdk

/**
 * Implementation of the ActivityResultContract interface specifically designed for handling
 * Yandex sign-in.
 */
class YandexSignInActivityResultContract :
    ActivityResultContract<YandexAuthSdk, Pair<Int, Intent?>>() {

    override fun createIntent(context: Context, input: YandexAuthSdk): Intent {
        val loginOptionsBuilder = YandexAuthLoginOptions.Builder()
        return input.createLoginIntent(loginOptionsBuilder.build())
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Pair<Int, Intent?> {
        return Pair(resultCode, intent)
    }
}