package matt.klib.commons

import matt.klib.lang.err
import matt.klib.sys.GAMING_WINDOWS
import matt.klib.sys.Machine
import matt.klib.sys.NEW_MAC
import matt.klib.sys.OLD_MAC
import matt.klib.sys.OPEN_MIND
import matt.klib.sys.WINDOWS_11_PAR_WORK
import matt.klib.sys.Windows
import java.io.BufferedReader
import java.io.InputStreamReader

val os: String by lazy { System.getProperty("os.name") }
val uname by lazy {
  if (thisMachine is Windows) null
  else run {
	val proc = ProcessBuilder("uname", "-m").start()
	BufferedReader(InputStreamReader(proc.inputStream)).readText().trim()
  }
}
val userName: String by lazy { System.getProperty("user.name") }
val thisMachine: Machine  by lazy {
  when {
	os == "Linux"        -> OPEN_MIND
	os.startsWith("Mac") -> when (uname) {
	  "arm64" -> NEW_MAC
	  else    -> OLD_MAC
	}

	else                 -> when (userName) {
	  "mgrot"        -> GAMING_WINDOWS
	  "matthewgroth" -> WINDOWS_11_PAR_WORK
	  else           -> err("machine?")
	}
  }
}

