rootProject.name = "nihon-go"

include(
    ":libraries:ftp-client",
    ":libraries:utils-library",
//        ":services:authentication-service",
    ":services:nihongo-dico-entries-service",
    ":services:nihongo-dico-sentences-service",
    ":services:nihongo-lessons-service",
//    ":web:frog-manager-web",
    ":web:nihongo-dico-web"
)

//project(":web:frog-manager-web").name = "nihongo-admin"
//project(":web:nihongo-dico-web").name = "nihongo-dico"


dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven {
            name = "JitPack"
            url = uri("https://jitpack.io")
        }
    }
}
