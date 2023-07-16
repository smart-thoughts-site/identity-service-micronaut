plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.0"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.9.0"
    id("com.google.devtools.ksp") version "1.9.0-1.0.11"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.0.0"
    id("io.micronaut.aot") version "4.0.0"
}

version = "0.1"
group = "site.smartthoughts"

val kotlinVersion= project.properties["kotlinVersion"]
repositories {
    mavenCentral()
}

dependencies {
    ksp("io.micronaut:micronaut-inject-java")
    ksp("io.micronaut.data:micronaut-data-processor")
    // ksp("io.micronaut.data:micronaut-data-hibernate-jpa")
    ksp("io.micronaut.security:micronaut-security-annotations")
    ksp("io.micronaut.serde:micronaut-serde-processor")

    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.reactor:micronaut-reactor")
    implementation("io.micronaut.security:micronaut-security-jwt")
    implementation("io.micronaut.serde:micronaut-serde-jackson")

    // implementation("io.micronaut.sql:micronaut-hibernate-jpa")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("jakarta.persistence:jakarta.persistence-api")

    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")

    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtimeOnly("org.yaml:snakeyaml")
    runtimeOnly("org.postgresql:postgresql")

    kspTest("io.micronaut:micronaut-inject-java")
    testImplementation("io.kotest:kotest-runner-junit5-jvm")
    testImplementation("io.micronaut.test:micronaut-test-kotest5")
    testImplementation("io.mockk:mockk")

    testImplementation("io.micronaut:micronaut-http-client")
}

application {
    mainClass.set("site.smartthoughts.identity.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("17")
}

tasks {
    compileKotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    compileTestKotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}
graalvmNative.toolchainDetection.set(false)
micronaut {
    runtime("netty")
    testRuntime("kotest5")
    processing {
        incremental(true)
        annotations("site.smartthoughts.identity.*")
    }
    aot {
        // Please review carefully the optimizations enabled below
        // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details
        optimizeServiceLoading.set(false)
        convertYamlToJava.set(false)
        precomputeOperations.set(true)
        cacheEnvironment.set(true)
        optimizeClassLoading.set(true)
        deduceEnvironment.set(true)
        optimizeNetty.set(true)
    }
}



