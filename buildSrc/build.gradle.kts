plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("io.micronaut.gradle:micronaut-gradle-plugin:3.5.3")
    implementation("com.bmuschko:gradle-docker-plugin:7.1.0")

    implementation("com.github.node-gradle:gradle-node-plugin:3.1.1")
    implementation("com.google.cloud.tools:jib-gradle-plugin:3.3.0")

    implementation("com.gorylenko.gradle-git-properties:gradle-git-properties:2.4.1")
}
