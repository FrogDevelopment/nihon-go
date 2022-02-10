plugins {
    jacoco
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

allprojects {
    group = "com.frogdevelopment.nihongo"
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(30, TimeUnit.MINUTES)
    resolutionStrategy.cacheDynamicVersionsFor(30, TimeUnit.SECONDS)
}


dependencies {
    // not using the "io.freefair.lombok" plugin because of some conflicts with micronaut annotation processor
    // read https://docs.micronaut.io/latest/guide/#lombok
    compileOnly("org.projectlombok:lombok:${Versions.lombok}")
    annotationProcessor("org.projectlombok:lombok:${Versions.lombok}")
    annotationProcessor(enforcedPlatform("io.micronaut:micronaut-bom:${Versions.micronaut}"))

    implementation(enforcedPlatform("io.micronaut:micronaut-bom:${Versions.micronaut}"))
    implementation("javax.annotation:javax.annotation-api")

    testImplementation(enforcedPlatform("org.junit:junit-bom:${Versions.junit}"))
    testImplementation(enforcedPlatform("org.testcontainers:testcontainers-bom:1.16.3"))

    testRuntimeOnly("org.junit.platform:junit-platform-launcher") {
        because("Only needed to run tests in a version of IntelliJ IDEA that bundles older versions")
    }
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.assertj:assertj-core")
}

tasks.named<Test>("test") {
    reports.html.required.set(false)

    useJUnitPlatform {
        includeTags("unitTest")
        includeTags("integrationTest")
    }

    finalizedBy(tasks.named("jacocoTestReport"))
}

tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.named("test"))
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(false)
    }
}

tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    violationRules {
        rule {
            limit {
                minimum = "0.9".toBigDecimal()
            }
        }
    }
}
