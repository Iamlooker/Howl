import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.6.21"
}

repositories {
	google()
	mavenCentral()
}

tasks.withType<KotlinCompile>().configureEach {
	kotlinOptions.apiVersion = "1.3"
}

dependencies {
	implementation("com.android.tools.build:gradle-api:7.2.1")
	implementation(kotlin("stdlib"))
	gradleApi()
}