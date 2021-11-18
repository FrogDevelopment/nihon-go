plugins {
    id("nihongo.service-convention")
}

dependencies {
    val tcPgroongaVersion = "1.1.2"

    implementation(project(":libraries:multi-schema-library"))
    implementation(project(":libraries:utils-library"))

    implementation("commons-io:commons-io:2.11.0")
    implementation("org.apache.commons:commons-compress:1.21")

    testImplementation("com.frog-development:testcontainers-pgroonga:$tcPgroongaVersion")
}
