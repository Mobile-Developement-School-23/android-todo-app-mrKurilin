package com.example.todoapp.data.remote

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

const val RETRY_COUNT = 3
const val WAIT_BETWEEN_RETRIES = 2000L

class RetryInterceptor @Inject constructor() : Interceptor {

    var tryCount = 0

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        return if (response.code == SERVER_ERROR_CODE) {

            if (tryCount < RETRY_COUNT) {
                Thread.sleep(WAIT_BETWEEN_RETRIES)
                tryCount++
                response.close()
                chain.call().clone().execute()
            }
            response.newBuilder()
                .code(SERVER_ERROR_CODE)
                .body("".toResponseBody(null))
                .protocol(Protocol.HTTP_2)
                .message("Network Error")
                .request(chain.request())
                .build()
        } else {
            response
        }
    }
}