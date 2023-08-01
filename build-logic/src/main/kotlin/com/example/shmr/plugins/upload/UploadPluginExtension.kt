package com.example.shmr.plugins.upload

import org.gradle.api.provider.Property
import java.math.BigDecimal

interface UploadPluginExtension{
    val enabledSizeCheck: Property<Boolean>
    val enabledApkDetailsSending: Property<Boolean>
    val maxSizeInMb: Property<BigDecimal>
}