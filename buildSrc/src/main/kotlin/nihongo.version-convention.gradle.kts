import java.io.ByteArrayOutputStream

tasks.register("tags") {
    group = "frog"
    doLast {
        println("Tags: ${rootProject.extra["tags"]}")
    }
}

val tags by extra {
    val tags = when (val branchName = getBranchName()) {
        "master" -> retrieveTagsForMaster()
        "develop" -> retrieveTagsForDevelop()
        else -> retrieveTagsForOther(branchName)
    }

    println("Computed version: $version, tags: $tags")

    tags
}

fun retrieveTagsForMaster(): Set<String> {
    val lastAnnotatedTag = getLastAnnotatedTag()
    allprojects {
        project.version = lastAnnotatedTag
    }
    return setOf(getCommitHash(), lastAnnotatedTag)
}

fun retrieveTagsForDevelop(): Set<String> {
    allprojects {
        project.version = "develop-SNAPSHOT"
    }
    return setOf(getCommitHash(), getShortCommitHash(), "develop")
}

fun retrieveTagsForOther(branchName: String): Set<String> {
    val branch = branchName
        .replace("-", "_")
        .replace("/", "-")

    allprojects {
        project.version = "$branch-SNAPSHOT"
    }

    return mutableSetOf(getCommitHash(), getShortCommitHash(), branch)
}

fun getBranchName(): String {
    return "git rev-parse --abbrev-ref HEAD".runCommand()
}

fun getLastAnnotatedTag(): String {
    return try {
        "git describe --abbrev=0".runCommand()
    } catch (e: Exception) {
        throw GradleException("No annotated tag found!", e)
    }
}

fun getCommitHash(): String {
    return "git rev-parse --verify HEAD".runCommand()
}

fun getShortCommitHash(): String {
    return "git rev-parse --verify --short HEAD".runCommand()
}

fun String.runCommand(currentWorkingDir: File = file("./")): String {
    val byteOut = ByteArrayOutputStream()
    project.exec {
        workingDir = currentWorkingDir
        commandLine = this@runCommand.split("\\s".toRegex())
        standardOutput = byteOut
    }
    return String(byteOut.toByteArray()).trim()
}
