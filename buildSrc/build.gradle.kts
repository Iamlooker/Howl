import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.7.0"
}

repositories {
	google()
	mavenCentral()
}

tasks.withType<KotlinCompile>().configureEach {
	kotlinOptions.apiVersion = "1.7"
}

dependencies {
	implementation("com.android.tools.build:gradle-api:7.3.1")
	implementation(kotlin("stdlib"))
	gradleApi()
}
