plugins {
    id("nihongo.library-convention")
}

dependencies {
    api(project(":libraries:multi-schema-library"))

    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-jdbc")

    api("commons-io:commons-io:2.11.0")
    api("org.apache.commons:commons-compress:1.21")
    api("commons-net:commons-net:3.8.0")

    compileOnly("org.postgresql:postgresql")

    testImplementation("org.mockftpserver:MockFtpServer:3.0.0")

}
