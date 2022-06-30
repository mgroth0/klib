@file:JvmName("KlibJvmKt")

package matt.klib

import java.awt.Color

fun helloJvm() = "hello from common jvm(${Color(1, 2, 3)}) lib on ${System.getProperty("os.name")}"

fun something() = 5
