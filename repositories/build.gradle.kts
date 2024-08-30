
plugins {
    alias(libs.plugins.kotlin.jpa)
}

dependencies {
    implementation(project(":core"))

    implementation(libs.spring.boot.starter.data.jpa)
}

