plugins {
    id("nihongo.service-convention")
}

dependencies {
    implementation(project(":libraries:utils-library"))

    implementation("org.apache.httpcomponents:httpclient-cache:4.5.13")
    implementation("org.apache.httpcomponents:httpmime:4.5.13")
    implementation("org.apache.httpcomponents:fluent-hc:4.5.13")

    testImplementation("org.testcontainers:junit-jupiter")
//    testImplementation("org.testcontainers:postgresql")
    testImplementation("com.frog-development:testcontainers-pgroonga:1.1.2")
}
