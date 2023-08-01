package com.example.shmr.plugins.upload

import com.example.shmr.TelegramApi
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import java.math.BigDecimal
import java.nio.file.Files

abstract class ApkSizeValidationTask : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @TaskAction
    fun execute() {
        val uploadPluginExtension = project.extensions.findByType(UploadPluginExtension::class.java)

        val maxSizeInMb = uploadPluginExtension?.maxSizeInMb?.get() ?: BigDecimal(20)
        val maxSizeInB = maxSizeInMb * BigDecimal(1024) * BigDecimal(1024)

        val apkFile = apkDir.get().asFile.listFiles()?.first {
            it.name.endsWith(".apk")
        } ?: error(".apk file not found")

        if (BigDecimal(Files.size(apkFile.toPath())) > maxSizeInB) {
            runBlocking {
                val api = TelegramApi()
                api.sendMessage("apk file is more than $maxSizeInMb mb")
                error("apk file is more than $maxSizeInMb mb")
            }
        }
    }
}