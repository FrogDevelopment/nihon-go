plugins {
    id("nihongo.service-convention")
}

dependencies {
    implementation(project(":libraries:utils-library"))

    testImplementation("com.frog-development:testcontainers-pgroonga:1.1.2")
    testImplementation("org.json:json:20211205")

}
