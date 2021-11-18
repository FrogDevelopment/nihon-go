import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("com.github.node-gradle.node")
}

node {
    version.set("14.13.1")
    npmVersion.set("6.14.8")
    download.set(false)
    npmInstallCommand.set(if ("true" == System.getenv("CI")) "ci" else "install")
}

tasks.register<NpmTask>("buildCI") {
    group = "frog"
    args.set(listOf("run", "build:prod"))

//    inputs.files(".browserslistrc", "angular.json", "package.json", "package-lock.json", "tsconfig.app.json", "tsconfig.json")
//    inputs.dir("nginx")
//    inputs.dir(fileTree("node_modules"))
//    inputs.dir(fileTree("src"))
//    outputs.dir("dist")
//    outputs.upToDateWhen { true }
}

tasks.register<Exec>("buildDockerImage") {
    dependsOn(tasks.named("buildCI"))
    group = "frog"

    doFirst {
        val arguments = mutableListOf<Any>()
        arguments.add("docker")
        arguments.add("build")
        val tags: Set<String> by rootProject.extra
        for (tag in tags) {
            arguments.add("-t")
            arguments.add("frognihongo/${project.name}:$tag")
        }
        arguments.add(".")
        commandLine(arguments)
    }
}

tasks.register<Exec>("pushDockerImages") {
//    dependsOn(tasks.named("buildDockerImage"))
    group = "frog"
    commandLine("docker", "push", "--all-tags", "frognihongo/${project.name}")
}

tasks.register<Delete>("clean") {
    group = "build"
    delete.add("dist")
}

