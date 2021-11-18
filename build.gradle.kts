import java.io.ByteArrayOutputStream

tasks.register("tags") {
    group = "frog"
    doLast {
        println("Tags: ${rootProject.extra["tags"]}")
    }
}

val tags by extra {
    when (val branchName = getBranchName()) {
        "master" -> retrieveTagsForMaster()
        "develop" -> retrieveTagsForDevelop()
        else -> retrieveTagsForOther(branchName)
    }
}

fun retrieveTagsForMaster(): Set<String> {
    val lastAnnotatedTag = getLastAnnotatedTag()
    project.version = lastAnnotatedTag
    return setOf(getCommitHash(), lastAnnotatedTag)
}

fun retrieveTagsForDevelop(): Set<String> {
    val version = "develop.${getShortCommitHash()}"
    project.version = version
    return setOf(getCommitHash(), "develop", version)
}

fun retrieveTagsForOther(branchName: String): Set<String> {
    val tags = mutableSetOf<String>()
    val branchMatchResult = """^(?<type>\w+)/(?<description>.*)$"""
        .toRegex()
        .matchEntire(branchName)!!
        .groups as MatchNamedGroupCollection

    val branchType = branchMatchResult["type"]!!.value
    val branchDescription = branchMatchResult["description"]!!.value

    tags.add(getCommitHash())
    tags.add("$branchType.$branchDescription")
    tags.add("$branchType.$branchDescription.${getShortCommitHash()}")
    tags.add("$branchType.$branchDescription.${countCommitsSince("develop")}")

    return tags
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

fun countCommitsSince(origin: String): String {
    return "git rev-list --no-merges --count HEAD ^origin/$origin".runCommand()
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

tasks.wrapper {
    gradleVersion = "7.3"
    distributionType = Wrapper.DistributionType.ALL
}
