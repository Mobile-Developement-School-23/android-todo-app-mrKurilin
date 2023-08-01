package com.example.shmr.plugins.upload

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized
import java.math.BigDecimal

class UploadPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create(
            "uploadPluginExtension",
            UploadPluginExtension::class.java
        ).apply {
            enabledSizeCheck.set(true)
            enabledApkDetailsSending.set(true)
            maxSizeInMb.set(BigDecimal(1))
        }

        val androidComponents = project.extensions.findByType(
            AndroidComponentsExtension::class.java
        ) ?: error("AndroidComponentsExtension not found")

        val appExtension = project.extensions.findByType(
            AppExtension::class.java
        ) ?: error("AppExtension not found")

        val uploadPluginExtension = project.extensions.findByType(UploadPluginExtension::class.java)

        androidComponents.onVariants { variant ->
            val variantName = variant.name.capitalized()
            val apkDirFromVariant = variant.artifacts.get(SingleArtifact.APK)

            val validateApkSize = project.tasks.register(
                "validateApkSizeFor$variantName",
                ApkSizeValidationTask::class.java
            ) {
                onlyIf {
                    uploadPluginExtension?.enabledSizeCheck?.get() ?: true
                }
                apkDir.set(apkDirFromVariant)
            }

            val sendApkDetail = project.tasks.register(
                "sendApkDetailFor$variantName",
                ApkDetailsSendingTask::class.java
            ) {
                onlyIf {
                    uploadPluginExtension?.enabledApkDetailsSending?.get() ?: true
                }
                apkDir.set(apkDirFromVariant)
            }

            project.tasks.register(
                "uploadApkFor$variantName",
                UploadTask::class.java
            ) {
                dependsOn(validateApkSize)

                apkDir.set(apkDirFromVariant)
                apkFileName.set("todolist-${variant.name}-${appExtension.defaultConfig.versionCode}.apk")

                finalizedBy(sendApkDetail)
            }
        }
    }
}