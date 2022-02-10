plugins {
    id("nihongo.version-convention")
}

tasks.wrapper {
    gradleVersion = "7.4.1"
    distributionType = Wrapper.DistributionType.ALL
}
