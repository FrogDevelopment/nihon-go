plugins {
    id("nihongo.version-convention")
}

tasks.wrapper {
    gradleVersion = "7.3.3"
    distributionType = Wrapper.DistributionType.ALL
}
