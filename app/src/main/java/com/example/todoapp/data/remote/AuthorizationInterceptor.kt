package com.example.todoapp.data.remote

import com.example.todoapp.TOKEN
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder().addHeader(
                "Authorization",
                "Bearer $TOKEN"
            ).build()
        )
    }
}