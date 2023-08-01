package com.example.shmr

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentDisposition
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.io.File

class TelegramApi(
    private val httpClient: HttpClient = HttpClient(OkHttp)
) {

    suspend fun uploadFile(file: File, fileName: String) {
        val headers = Headers.build {
            append(
                HttpHeaders.ContentDisposition,
                "${ContentDisposition.Parameters.FileName}=$fileName"
            )
        }

        val body = MultiPartFormDataContent(
            formData { append(key = "document", value = file.readBytes(), headers = headers) }
        )

        httpClient.post("https://api.telegram.org/bot$TG_TOKEN/sendDocument") {
            parameter("chat_id", TG_CHAT_ID)
            setBody(body)
        }
    }

    suspend fun sendMessage(text: String) {
        httpClient.post("https://api.telegram.org/bot$TG_TOKEN/sendMessage") {
            parameter("chat_id", TG_CHAT_ID)
            parameter("text", text)
        }
    }
}