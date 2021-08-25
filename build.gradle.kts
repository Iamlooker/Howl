buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Libs.Classpath.gradleClasspath)
        classpath(Libs.Classpath.gradleKotlin)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}