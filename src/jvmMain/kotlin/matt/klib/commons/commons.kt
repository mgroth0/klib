package matt.klib.commons

import matt.klib.sys.Machine.NEW_MAC
import matt.klib.sys.Machine.OLD_MAC
import matt.klib.sys.Machine.WINDOWS
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

val os: String by lazy { System.getProperty("os.name") }
val ismac by lazy { os.startsWith("Mac") }

val thisMachine by lazy {
  when {
	ismac -> when {
	  isNewMac -> NEW_MAC
	  else     -> OLD_MAC
	}
	else  -> WINDOWS
  } // TODO: CHECK LINUX
}


val isNewMac by lazy {
  ismac && run {
	val proc = ProcessBuilder("uname", "-m").start()
	BufferedReader(InputStreamReader(proc.inputStream)).readText().trim()
  } == "arm64"
}

operator fun File.get(item: String): File {
  return resolve(item)
}
operator fun File.get(item: Char): File {
  return resolve(item.toString())
}

val USER_HOME = File(thisMachine.homeDir)
val REGISTERED_FOLDER = USER_HOME[thisMachine.registeredDir]
val FLOW_FOLDER = thisMachine.flowFolder?.let { REGISTERED_FOLDER[it] }
val DATA_FOLDER = REGISTERED_FOLDER.resolve("data")
val LOG_FOLDER = FLOW_FOLDER?.resolve("log")?.apply { mkdir() }
val USER_DIR = File(System.getProperty("user.dir"))
val TEMP_DIR = USER_DIR["tmp"].apply { mkdir() }
val DNN_FOLDER = when (thisMachine) {
  NEW_MAC -> REGISTERED_FOLDER["ide/dnn"]
  OLD_MAC -> REGISTERED_FOLDER["todo/science/dnn"]
  WINDOWS -> null
}
val WINDOW_GEOMETRY_FOLDER = DATA_FOLDER["window"]
val SETTINGS_FOLDER = DATA_FOLDER["settings"]
val VAL_JSON = DATA_FOLDER.resolve("VAL.json")
val VAR_JSON = DATA_FOLDER["VAR.json"]
val SCREENSHOT_FOLDER = REGISTERED_FOLDER["screenshots"]
val CACHE_FOLDER = REGISTERED_FOLDER["cache"]
val KJG_DATA_FOLDER = DATA_FOLDER.resolve("kjg")

const val DS_STORE = ".DS_Store"