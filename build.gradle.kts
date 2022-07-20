/*NPM INSTALL TASK IS DISABLED IN ROOT BUILDSCRIPT BECAUSE IT PRODUCES OBNOXIOUS WARNING. WILL NEED TO ENABLE THAT TO INSTALL DEPENDENCIES PROBABLY*/


repositories {
  mavenCentral()
  maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}



dependencies {
  commonMainImplementation(libs.kotlinx.serialization.json)
  commonMainImplementation(libs.kotlinx.html.common)
  //  jsMainApi(libs.kotlinx.html.js)
  jsMainImplementation(libs.kotlinx.html.js)
  //  jvmMainApi(libs.kotlinx.html.jvm)
  jvmMainImplementation(libs.kotlinx.html.jvm)

  jvmMainImplementation(projects.k.kjlib.lang)
}

//implementations(
//
//)
//implementations(
//  implementation("org.jetbrains.kotlinx:kotlinx-html-common:0.7.3")
//)

plugins {
  kotlin("plugin.serialization")

  /*experimental.coroutines = org.jetbrains.kotlin.gradle.dsl.Coroutines.ENABLE*/
}