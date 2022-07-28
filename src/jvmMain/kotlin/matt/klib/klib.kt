@file:JvmName("KlibJvmKt")

package matt.klib

import java.awt.Color
import java.io.IOException

import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection


fun helloJvm() = "hello from common jvm(${Color(1, 2, 3)}) lib on ${System.getProperty("os.name")}"

fun something() = 5


 fun netIsAvailable(): Boolean {
  return try {
	val url = URL("http://www.google.com")
	val conn: URLConnection = url.openConnection()
	conn.connect()
	conn.getInputStream().close()
	true
  } catch (e: MalformedURLException) {
	throw RuntimeException(e)
  } catch (e: IOException) {
	false
  }
}