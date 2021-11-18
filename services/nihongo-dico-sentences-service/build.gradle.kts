plugins {
    id("nihongo.service-convention")
}

dependencies {
    val tcPgroongaVersion = "1.1.2"

    implementation(project(":libraries:multi-schema-library"))
    implementation(project(":libraries:utils-library"))

    implementation("org.apache.httpcomponents:httpclient-cache")
    implementation("org.apache.httpcomponents:httpmime")
    implementation("org.apache.httpcomponents:fluent-hc")

    testImplementation("com.frog-development:testcontainers-pgroonga:$tcPgroongaVersion")
}
