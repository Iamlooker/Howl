buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Classpath.gradleClasspath)
        classpath(Classpath.gradleKotlin)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}