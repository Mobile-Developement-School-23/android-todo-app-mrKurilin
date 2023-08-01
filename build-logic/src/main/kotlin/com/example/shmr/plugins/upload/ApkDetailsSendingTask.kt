package com.example.shmr.plugins.upload

import com.example.shmr.TelegramApi
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import java.util.Locale
import java.util.zip.ZipFile

abstract class ApkDetailsSendingTask : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @TaskAction
    fun execute() {
        val api = TelegramApi()

        val apkFile = apkDir.get().asFile.listFiles()?.first {
            it.name.endsWith(".apk")
        } ?: error(".apk file not found")

        var message = "Apk file content:\n"

        ZipFile(apkFile).use { zipFile ->
            val entries = zipFile.entries()
            for (entry in entries) {
                if (!entry.name.contains("/")) {
                    val entryInfo = "${entry.name} %,.2f MB".format(
                        Locale.ENGLISH,
                        entry.size.toDouble() / 1024 / 1024
                    )
                    message += "$entryInfo\n"
                }
            }
        }

        runBlocking {
            api.sendMessage(message)
        }
    }
}