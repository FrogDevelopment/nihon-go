plugins {
    `java-library`
    id("nihongo.base-convention")
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_15
    targetCompatibility = JavaVersion.VERSION_15
}

dependencies {

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.apache.commons:commons-lang3")

    api("org.springframework.cloud:spring-cloud-starter-openfeign")
    api("io.github.openfeign:feign-httpclient")
    api("io.github.openfeign:feign-jackson")

    api("com.frog-development:authorization-jwt-module:1.4.1")
    api("org.springframework.boot:spring-boot-starter-security")

    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("org.junit.vintage", "junit-vintage-engine")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-params")

    testRuntimeOnly("org.junit.platform:junit-platform-runner")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.bootJar {
    enabled = false
}

tasks.bootBuildImage {
    enabled = false
}

tasks.bootRun {
    enabled = false
}

tasks.jar {
    enabled = true
}

springBoot {
    buildInfo()
}
