tasks.register("clean", Delete::class) {
    group = "build"
    description = "Deletes the build directory."

    delete(rootProject.layout.buildDirectory.get().asFile)
}

tasks.register("assembleDebugRelease") {
    group = "build"
    description = "Assembles both debug and release builds of the app module."
    dependsOn(":app:assembleDebug", ":app:assembleRelease")
}

tasks.register("cleanBuild") {
    group = "build"
    description = "Cleans the project and then assembles all builds in the app module."
    dependsOn("clean", "assembleDebugRelease")
}

tasks.register("printVersionInfo") {
    group = "build"
    description = "Prints the version information of the project."
    doLast {
        println("Version Code: ${project.version}")
        println("Version Name: ${project.findProperty("versionName") ?: "N/A"}")
    }
}
