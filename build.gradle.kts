buildscript {
	repositories {
		google()
		mavenCentral()
	}
	dependencies {
		classpath(Classpath.gradleClasspath)
		classpath(Classpath.gradleKotlin)
		classpath(Hilt.hiltClasspath)
	}
}

tasks.register("clean", Delete::class) {
	delete(rootProject.buildDir)
}