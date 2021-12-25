apply(plugin = "org.jetbrains.kotlin.jvm")
apply(plugin = "java-library")

configure<JavaPluginExtension> {
	sourceCompatibility = JavaVersion.VERSION_11
	targetCompatibility = JavaVersion.VERSION_11
}