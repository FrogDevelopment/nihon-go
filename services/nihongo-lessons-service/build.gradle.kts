plugins {
    id("nihongo.service-convention")
}

dependencies {
    implementation(project(":libraries:ftp-client"))
    implementation(project(":libraries:utils-library"))

    implementation("org.apache.commons:commons-csv:1.9.0")
}
