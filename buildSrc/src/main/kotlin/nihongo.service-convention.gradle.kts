plugins {
    id("io.micronaut.minimal.application")
    id("nihongo.base-convention")
    id("com.google.cloud.tools.jib")
}

micronaut {
    version.set(Versions.micronaut)
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(false)
        annotations("com.frogdevelopment.nihongo.*")
    }
}

dependencies {
    annotationProcessor("io.micronaut.data:micronaut-data-processor")
    annotationProcessor("io.micronaut:micronaut-inject-java")
    annotationProcessor("io.micronaut:micronaut-validation")
    annotationProcessor("io.micronaut:micronaut-http-validation")

    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut:micronaut-management")

//    runtimeOnly("io.micronaut.kubernetes:micronaut-kubernetes-discovery-client")
    implementation("io.micronaut.discovery:micronaut-discovery-client")
    runtimeOnly("ch.qos.logback:logback-classic")

    implementation("org.jetbrains:annotations:23.0.0")

//    implementation("com.frog-development:authorization-jwt-module:1.4.0")

    implementation("io.micronaut.data:micronaut-data-jdbc")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.flyway:micronaut-flyway")
    implementation("org.postgresql:postgresql")

//    implementation("net.logstash.logback:logstash-logback-encoder:6.5")

    implementation("org.apache.commons:commons-lang3:3.12.0")

    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")
}

jib {
    from {
        image = "eclipse-temurin:17.0.4.1_1-jre-jammy"
    }
    to {
        image = "frognihongo/${name}"
    }
    container {
        jvmFlags = listOf("-Xmx128m")
        volumes = listOf("/tmp")
        creationTime = "USE_CURRENT_TIMESTAMP"
        labels.put("frog.image_base", jib.from.image.toString())
    }
}
