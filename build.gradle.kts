plugins {
    id("nihongo.version-convention")
}


tasks.wrapper {
    gradleVersion = "7.3.1"
    distributionType = Wrapper.DistributionType.ALL
}
