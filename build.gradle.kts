/*NPM INSTALL TASK IS DISABLED IN ROOT BUILDSCRIPT BECAUSE IT PRODUCES OBNOXIOUS WARNING. WILL NEED TO ENABLE THAT TO INSTALL DEPENDENCIES PROBABLY*/

modtype = NO_NATIVE

repositories {
  mavenCentral()
  maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

dependencies {
  //  commonMainCompileOnly(libs.kotlinx.html.common)
  commonMainImplementation(libs.kotlinx.html.common)
  //  jsMainApi(libs.kotlinx.html.js)
  jsMainImplementation(libs.kotlinx.html.js)
  //  jvmMainApi(libs.kotlinx.html.jvm)
  jvmMainImplementation(libs.kotlinx.html.jvm)
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