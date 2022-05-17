buildscript {
	repositories {
		google()
		mavenCentral()
	}
	dependencies {
        classpath("com.android.tools.build:gradle:7.2.0")
		classpath(Classpath.gradleKotlin)
        classpath(Hilt.hiltClasspath)
	}
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}