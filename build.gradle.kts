plugins {
	`java-library`
	alias(libs.plugins.loomID) // Fabric Loom
}

// Project identification pulled from gradle/libs.versions.toml
group = libs.versions.mavenGroup
version = libs.versions.modVersion.get()

// Sets the output filename format
base {
	archivesName.set(libs.versions.modName.zip(libs.versions.minecraft) { name, mc -> "$name-$mc" })
}

// Automatically downloads and uses the correct Java version for this project
java {
	toolchain {
		languageVersion.set(libs.versions.java.map { JavaLanguageVersion.of(it) })
		vendor.set(JvmVendorSpec.ADOPTIUM)
	}
}

// Essential Minecraft and Fabric dependencies
dependencies {
	minecraft(libs.minecraft)
	implementation(libs.fabric.loader)
	implementation(libs.fabric.api)
}

// Replaces placeholders in fabric.mod.json and pack.mcmeta with variables defined in gradle/libs.versions.toml
tasks.processResources {
	val modProperties = mapOf(
		"version" to version,
		"minecraftVersion" to libs.versions.minecraft,
		"loaderVersion" to libs.versions.fabricLoader,
		"javaVersion" to libs.versions.java.map(String::toInt),
		"packFormat" to libs.versions.pack
	)
	
    inputs.properties(modProperties)

	// Apply the variables to the JSON and Metadata files
    filesMatching(listOf("fabric.mod.json", "**/pack.mcmeta")) {
        expand(modProperties)
    }
}

// Ensure the compiler targets the specific Java version and uses UTF-8
tasks.withType<JavaCompile>().configureEach {
	options.release.set(libs.versions.java.map { it.toInt() })
	options.encoding = "UTF-8"
}

// Rename the LICENSE file in the final JAR
tasks.jar {
	from("LICENSE") {
		rename { "${it}_${archiveBaseName.get()}" }
	}
}











