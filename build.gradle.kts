plugins {
	`java-library`
	alias(libs.plugins.loomID) // Fabric Loom
}

// Project identification pulled from gradle/libs.versions.toml
group = libs.versions.mavenGroup.get()
version = libs.versions.modVersion.get()

// Sets the output filename format
base {
	archivesName.set("${libs.versions.modName.get()}-${libs.versions.minecraft.get()}")
}

// Automatically downloads and uses the correct Java version for this project
java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
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
		"minecraftVersion" to libs.versions.minecraft.get(),
		"loaderVersion" to libs.versions.fabricLoader.get(),
		"javaVersion" to libs.versions.java.get().toInt(),
		"packFormat" to libs.versions.pack.get()
	)
	
    inputs.properties(modProperties)

	// Apply the variables to the JSON and Metadata files
    filesMatching(listOf("fabric.mod.json", "**/pack.mcmeta")) {
        expand(modProperties)
    }
}

// Ensure the compiler targets the specific Java version and uses UTF-8
tasks.withType<JavaCompile>().configureEach {
	options.release.set(libs.versions.java.get().toInt())
	options.encoding = "UTF-8"
}

// Rename the LICENSE file in the final JAR
tasks.jar {
	val archiveNameProvider = archiveBaseName
	from("LICENSE") {
		rename { filename -> "${filename}_${archiveNameProvider.get()}" }
	}
}



