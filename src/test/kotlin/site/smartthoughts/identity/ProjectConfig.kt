package site.smartthoughts.identity

import io.kotest.core.config.AbstractProjectConfig
import io.micronaut.test.extensions.kotest5.MicronautKotest5Extension

@Suppress("unused")

class ProjectConfig : AbstractProjectConfig() {
    override fun extensions(): List<MicronautKotest5Extension> {
        return listOf(MicronautKotest5Extension)
    }
}