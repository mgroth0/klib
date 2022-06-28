package matt.klib.commons

import matt.klib.sys.NEW_MAC
import matt.klib.sys.OLD_MAC
import matt.klib.sys.WINDOWS
import java.io.BufferedReader
import java.io.InputStreamReader

val os: String by lazy { System.getProperty("os.name") }
val uname by lazy {
  val proc = ProcessBuilder("uname", "-m").start()
  BufferedReader(InputStreamReader(proc.inputStream)).readText().trim()
}
val thisMachine by lazy {
  when {
	os.startsWith("Mac") -> when {
	  uname == "arm64" -> NEW_MAC
	  else             -> OLD_MAC
	}

	else                 -> WINDOWS
  } // TODO: CHECK LINUX
}

