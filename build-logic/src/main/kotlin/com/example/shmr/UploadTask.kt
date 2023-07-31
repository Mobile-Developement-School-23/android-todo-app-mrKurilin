package com.example.shmr

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

abstract class UploadTask : DefaultTask() {

    @get:InputDirectory
    abstract val apkDirectory: DirectoryProperty

    @TaskAction
    fun upload() {
        val api = TelegramApi(HttpClient(OkHttp))
        runBlocking {
            apkDirectory.get().asFile.listFiles()
                .filter { it.name.endsWith(".apk") }
                .forEach { apkFile ->
                    api.uploadFile(file = apkFile)
                }
        }
    }
}