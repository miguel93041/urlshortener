plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.detekt)
}

subprojects {
    apply(plugin = rootProject.libs.plugins.kotlin.asProvider().get().pluginId)

    kotlin {
        jvmToolchain(17)
    }

    repositories {
        mavenCentral()
    }

    tasks {
        test {
            useJUnitPlatform()
        }
    }

    dependencies {
        implementation(platform(rootProject.libs.spring.boot.bom))

        testImplementation(rootProject.libs.kotlin.test)
        testImplementation(rootProject.libs.mockito.kotlin)
    }
}

repositories {
    mavenCentral()
}

tasks.check {
    dependsOn("detekt")
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(platform(libs.spring.boot.bom))
    testImplementation(rootProject.libs.junit.jupiter)
    testRuntimeOnly(rootProject.libs.junit.platform.launcher)
}
