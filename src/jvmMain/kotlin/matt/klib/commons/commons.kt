package matt.klib.commons

import matt.klib.file.MFile
import matt.klib.file.ext.resolve
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


operator fun MFile.get(item: String): MFile {
  return resolve(item)
}

operator fun MFile.get(item: Char): MFile {
  return resolve(item.toString())
}

operator fun MFile.plus(item: String): MFile {
  return resolve(item)
}

operator fun MFile.plus(item: Char): MFile {
  return resolve(item.toString())
}

val USER_HOME = MFile(thisMachine.homeDir)
val REGISTERED_FOLDER = USER_HOME[thisMachine.registeredDir]
val BIN_FOLDER = REGISTERED_FOLDER + "bin"
val APPLESCRIPT_FOLDER = (BIN_FOLDER + "applescript").apply { mkdirs() }
val FLOW_FOLDER = thisMachine.flowFolder?.let { REGISTERED_FOLDER[it] }
val KCOMP_FOLDER = FLOW_FOLDER!!.parentFile!!["kcomp"]
val DATA_FOLDER = REGISTERED_FOLDER.resolve("data")
val LOG_FOLDER = REGISTERED_FOLDER["log"].apply { mkdir() }
val USER_DIR = MFile(System.getProperty("user.dir"))
val TEMP_DIR = USER_DIR["tmp"].apply { mkdir() }
val DNN_FOLDER = when (thisMachine) {
  NEW_MAC -> REGISTERED_FOLDER["ide/dnn"]
  OLD_MAC -> REGISTERED_FOLDER["todo/science/dnn"]
  WINDOWS -> null
}
val HEP_FOLDER = when (thisMachine) {
  NEW_MAC -> REGISTERED_FOLDER["ide/hep"]
  OLD_MAC -> REGISTERED_FOLDER["todo/science/hep"]
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

val OPEN_NEAREST_GRADLE_BUILDSCRIPT = "OPEN_NEAREST_GRADLE_BUILDSCRIPT"
val OPEN_NEARST_KOTLIN_DESCENDENT = "OPEN_NEARST_KOTLIN_DESCENDENT"

