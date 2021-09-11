buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Classpath.gradleClasspath)
        classpath(Classpath.gradleKotlin)
        classpath(Classpath.hiltClasspath)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}