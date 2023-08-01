package com.example.shmr.plugins.upload

import com.example.shmr.TelegramApi
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files
import java.text.DecimalFormat

abstract class UploadTask : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val apkFileName: Property<String>

    @TaskAction
    fun upload() {
        val api = TelegramApi()

        val apkFile = apkDir.get().asFile.listFiles()?.first {
            it.name.endsWith(".apk")
        } ?: error(".apk file not found")

        runBlocking {
            api.uploadFile(file = apkFile, fileName = apkFileName.get())
            val fileSize = Files.size(apkFile.toPath()).toDouble() / 1024 / 1024
            val fileSizeMb = DecimalFormat("#.##").format(fileSize)
            api.sendMessage("Apk file size is $fileSizeMb mb")
        }
    }
}