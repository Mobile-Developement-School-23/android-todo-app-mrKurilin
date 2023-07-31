package com.example.shmr

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized

class UploadPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidComponents = project.extensions.findByType(AndroidComponentsExtension::class.java)
            ?: error("android plugin not found")

        androidComponents.onVariants { variant ->
            val variantName = variant.name.capitalized()
            val apkDirFromVariant = variant.artifacts.get(SingleArtifact.APK)
            project.tasks.register("uploadApkFor$variantName", UploadTask::class.java) {
                apkDirectory.set(apkDirFromVariant)
            }
        }
    }
}