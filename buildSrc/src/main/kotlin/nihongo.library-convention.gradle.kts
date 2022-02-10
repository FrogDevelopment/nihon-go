import Versions.micronaut

plugins {
    id("nihongo.base-convention")
    id("io.micronaut.minimal.library")
}

micronaut {
    version.set(Versions.micronaut)
    runtime("none")
    testRuntime("junit5")
}

dependencies {
    testRuntimeOnly("ch.qos.logback:logback-classic")
}
