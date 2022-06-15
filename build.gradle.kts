buildscript {
	repositories {
		google()
		mavenCentral()
	}
	dependencies {
        classpath("com.android.tools.build:gradle:7.2.1")
		classpath(Classpath.gradleKotlin)
        classpath(Hilt.classpath)
	}
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}