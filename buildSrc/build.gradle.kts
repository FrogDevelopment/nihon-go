plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("io.freefair.gradle:lombok-plugin:6.3.0")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.6.2")
    implementation("io.spring.gradle:dependency-management-plugin:1.0.11.RELEASE")
    implementation("gradle.plugin.com.google.cloud.tools:jib-gradle-plugin:3.1.4")

    implementation("com.github.node-gradle:gradle-node-plugin:3.1.1")
}
