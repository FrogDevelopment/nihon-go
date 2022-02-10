plugins {
    id("nihongo.library-convention")
}

dependencies {
    api("commons-net:commons-net:3.8.0")
    api("com.hierynomus:sshj:0.34.0")

    testImplementation("org.mockftpserver:MockFtpServer:3.0.0")
}
