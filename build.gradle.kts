/*NPM INSTALL TASK IS DISABLED IN ROOT BUILDSCRIPT BECAUSE IT PRODUCES OBNOXIOUS WARNING. WILL NEED TO ENABLE THAT TO INSTALL DEPENDENCIES PROBABLY*/

plugins {
  kotlin("plugin.serialization") version tomlVersion("kotlin")

  /*experimental.coroutines = org.jetbrains.kotlin.gradle.dsl.Coroutines.ENABLE*/
}