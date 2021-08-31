/**
 * https://tomgregory.com/automating-docker-builds-with-gradle/
 *
 */

plugins {
    base
}

version = "0.5.3"

tasks {

    task<Copy>("configureJanusgraph2DynamicServer") {
        group = "compose"
        from(layout.projectDirectory.dir("src"))
        into(layout.buildDirectory)
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
//        expand("dockerImage" to "docker.io/babeloff/janusgraph2:latest")
        expand(
            "dockerImage" to "janusgraph2",
            "dockerImageVersion" to "0.5.3")
    }

    task<Exec>("downJanusgraph2DynamicServer") {
//        logger.quiet("docker compose up task $path")
        executable = "docker"
        group = "compose"
        environment("COMPOSE_PROJECT_NAME", "janusgraph-dynamic-server")
        args(listOf(
            "compose",
            "-f",
            layout.buildDirectory.file("docker-compose.yaml").get().asFile.path,
            "down"
        ))
    }

    task<Exec>("upJanusgraph2DynamicServer") {
        dependsOn(
            ":janusgraph2:dockerJanusgraph2Build",
            ":docker-compose:dockerCreateJGServerVolumes")
//        logger.quiet("docker compose up task $path")
        executable = "docker"
        group = "compose"
        environment("COMPOSE_PROJECT_NAME", "janusgraph-dynamic-server")
        args(listOf(
            "compose",
            "-f",
            layout.buildDirectory.file("docker-compose.yaml").get().asFile.path,
            "up"
        ))
    }
}

