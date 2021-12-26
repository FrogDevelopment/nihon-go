rootProject.name = "nihon-go"

include(
    ":libraries:multi-schema-library",
    ":libraries:utils-library",
//        "services:authentication-service",
    "services:nihongo-dico-entries-service",
    "services:nihongo-dico-sentences-service",
    "services:nihongo-lessons-service",
//        "web:frog-manager-web",
//        "web:nihongo-dico-web"
)

//project(":front:frog-manager-web").name = "nihongo-dico"
//project(":front:nihongo-dico-web").name = "nihongo-admin"
