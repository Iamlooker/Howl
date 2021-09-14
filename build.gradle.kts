buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Classpath.gradleClasspath)
        classpath(Classpath.gradleKotlin)
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.38.1")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}