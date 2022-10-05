plugins {
    id("nihongo.library-convention")
}

dependencies {
    implementation(project(":libraries:ftp-client"))
    api("commons-io:commons-io:2.11.0")
    api("org.apache.commons:commons-compress:1.21")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.reactor:micronaut-reactor")

    implementation("io.micronaut.discovery:micronaut-discovery-client")

    implementation("org.postgresql:postgresql")

}
