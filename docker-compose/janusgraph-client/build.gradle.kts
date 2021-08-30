/**
 * https://tomgregory.com/automating-docker-builds-with-gradle/
 *
 */

plugins {
    base
}

version = "0.5.3"

//dependencies {
//    docker (project(":docker-image"))
//}

tasks {

    named<Delete>("clean") {
//        delete.add("build")
    }

    task<Copy>("configureJanusgraph2Client") {
        group = "compose"
        from(layout.projectDirectory.dir("src"))
        into(layout.buildDirectory)
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
//        expand("dockerImage" to "docker.io/babeloff/janusgraph2:latest")
        expand("dockerImage" to "janusgraph2:latest")
    }

    task<Exec>("downJanusgraph2Client") {
        logger.quiet("docker compose up task $path")
        executable = "docker"
        group = "compose"
        args(listOf(
            "compose",
            "-f",
            layout.buildDirectory.file("docker-compose.yaml").get().asFile.path,
            "down"
        ))
    }

    task<Exec>("upJanusgraph2Client") {
        dependsOn(
            ":janusgraph2:dockerJanusgraph2Build",
            ":docker-compose:dockerCreateJGClientVolumes")
        logger.quiet("docker compose up task $path")
        executable = "docker"
        group = "compose"
        args(listOf(
            "compose",
            "-f",
            layout.buildDirectory.file("docker-compose.yaml").get().asFile.path,
            "up"
        ))
    }
}
