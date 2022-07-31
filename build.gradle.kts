buildscript {
	dependencies {
		classpath(Hilt.classpath)
	}
}

plugins {
	id("com.android.application") version "7.2.1" apply false
	id("com.android.library") version "7.2.1" apply false
	id("org.jetbrains.kotlin.android") version Kotlin.version apply false
	id("com.google.devtools.ksp") version Kotlin.kspVersion apply false
}

tasks.register("clean", Delete::class) {
	delete(rootProject.buildDir)
}