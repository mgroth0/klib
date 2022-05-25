@file:JvmName("KlibJvmKt")

package matt.klib

import java.io.File

val USER_HOME = File(System.getProperty("user.home"))
val REGISTERED_FOLDER = File(USER_HOME.resolve(".registeredDir.txt").readText().trim())