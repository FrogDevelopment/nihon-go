plugins {
    jacoco
    id("io.freefair.lombok")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

allprojects {
    group = "com.frogdevelopment.nihongo"
}

repositories {
    mavenCentral()
    maven {
        name = "JitPack"
        url = uri("https://jitpack.io")
    }
}

dependencyManagement {
    imports {
        mavenBom("org.junit:junit-bom:5.8.1")
        mavenBom("org.testcontainers:testcontainers-bom:1.16.2")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.0")
    }
}

configurations {
    all {
        resolutionStrategy.cacheChangingModulesFor(10, "seconds")
    }
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
