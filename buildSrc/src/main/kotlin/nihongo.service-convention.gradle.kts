plugins {
    java
    id("nihongo.base-convention")
    id("com.google.cloud.tools.jib")
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_15
    targetCompatibility = JavaVersion.VERSION_15
}

dependencies {
    val authorizationJwtVersion = "1.4.0"
    val dockerSecretsVersion = "1.0.0"

    annotationProcessor("org.springframework:spring-context-indexer")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.jetbrains:annotations:22.0.0")

    implementation("com.frog-development:authorization-jwt-module:$authorizationJwtVersion")
    implementation("com.frog-development:docker-secrets-module:$dockerSecretsVersion")

    implementation("org.postgresql:postgresql")
    implementation("org.hibernate:hibernate-java8")
    implementation("org.flywaydb:flyway-core")

    implementation("org.springframework.boot:spring-boot-actuator-autoconfigure")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")

    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine")

    implementation("org.jolokia:jolokia-core")
    implementation("net.logstash.logback:logstash-logback-encoder:6.5")

    implementation("javax.validation:validation-api")
    implementation("org.apache.commons:commons-lang3")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("org.junit.vintage", "junit-vintage-engine")
    }
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-params")

    testRuntimeOnly("org.junit.platform:junit-platform-runner")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

springBoot {
    buildInfo()
}

jib {
    from {
        image = "frogdevelopment/docker-adoptopenjdk15-curl:latest"
    }
    to {
        image = "frognihongo/${name}"
        @Suppress("UNCHECKED_CAST")
        tags = rootProject.extra["tags"] as Set<String>
    }
    container {
        jvmFlags = listOf("-Xmx128m")
        volumes = listOf("/tmp")
        creationTime = "USE_CURRENT_TIMESTAMP"
        labels.put("frog.image_base", jib.from.image.toString())
    }
}
