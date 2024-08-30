plugins {
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.spring)
}

dependencies {
    implementation(project(":core"))

    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.hateoas)
    implementation(libs.commons.validator)
    implementation(libs.guava)

    testImplementation(rootProject.libs.spring.boot.starter.test)
}