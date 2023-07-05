package com.example.todoapp.data.remote

import android.content.SharedPreferences
import com.example.todoapp.data.local.TOKEN_KEY
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPreferences.getString(TOKEN_KEY, "")
        return chain.proceed(
            chain.request().newBuilder().addHeader(
                "Authorization",
                "OAuth $token"
            ).build()
        )
    }
}