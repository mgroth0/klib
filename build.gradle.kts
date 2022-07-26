import matt.file.kt

/*NPM INSTALL TASK IS DISABLED IN ROOT BUILDSCRIPT BECAUSE IT PRODUCES OBNOXIOUS WARNING. WILL NEED TO ENABLE THAT TO INSTALL DEPENDENCIES PROBABLY*/


repositories {
  mavenCentral()
  maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

dependencies {
  commonMainImplementation(libs.kotlinx.serialization.json)
  commonMainImplementation(libs.kotlinx.html.common)
  jsMainImplementation(libs.kotlinx.html.js)
  jvmMainImplementation(libs.kotlinx.html.jvm)
}

generateKt(
  matt.mstruct.SourceSets.commonMain,
  matt.file.mFile("matt") + "klib" + "icongen" + "icongen".kt
) {
  matt.mstruct.kt.KotlinCode(
	packageStatement = "  package matt.klib.icongen",
	code = """
       enum class Icon {
    ${
	  matt.file.commons.ICON_FOLDER.listFiles()!!.filterIsInstance<matt.file.ImageFile>()
		.joinToString(",") { matt.mstruct.kt.safeKtName(it.nameWithoutExtension) }
	}
  }
    """.trimIndent()
  )
}