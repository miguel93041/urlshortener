plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.kotlin.spring)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":delivery"))
    implementation(project(":repositories"))

    implementation(libs.spring.boot.starter)
    implementation(libs.bootstrap)
    implementation(libs.jquery)

    runtimeOnly(libs.hsqldb)
    runtimeOnly(libs.kotlin.reflect)

    testImplementation(rootProject.libs.spring.boot.starter.test)
    testImplementation(libs.spring.boot.starter.web)
    testImplementation(libs.spring.boot.starter.jdbc)
    testImplementation(libs.httpclient5)
}
