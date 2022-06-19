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

///*need things like this to all be in objects because they are instantiated lazily, and therefore wont be a memory leak issue when trying to have dynamic intelliJ plugins... in general this is definitely the best design and I'm sure this pattern has even broader advantages*/
//object CommonFiles {
val USER_HOME = MFile(thisMachine.homeDir)
val REGISTERED_FOLDER = USER_HOME[thisMachine.registeredDir]
val BIN_FOLDER = REGISTERED_FOLDER + "bin"
val APPLESCRIPT_FOLDER = (BIN_FOLDER + "applescript").apply { mkdirs() }
val IDE_FOLDER = REGISTERED_FOLDER["IDE"]
val DATA_FOLDER = REGISTERED_FOLDER.resolve("data")
val SOUND_FOLDER = REGISTERED_FOLDER + "sound"
val LOG_FOLDER = REGISTERED_FOLDER["log"].apply { mkdir() }
val USER_DIR = MFile(System.getProperty("user.dir"))
val TEMP_DIR by lazy { REGISTERED_FOLDER["tmp"].apply { mkdir() } }
val WINDOW_GEOMETRY_FOLDER = DATA_FOLDER["window"]
val SETTINGS_FOLDER = DATA_FOLDER["settings"]
val VAL_JSON = DATA_FOLDER.resolve("VAL.json")
val VAR_JSON = DATA_FOLDER["VAR.json"]
val SCREENSHOT_FOLDER = REGISTERED_FOLDER["screenshots"]
val CACHE_FOLDER = REGISTERED_FOLDER["cache"]
val KJG_DATA_FOLDER = DATA_FOLDER.resolve("kjg")
//}

val REL_ROOT_FILES = MFile("RootFiles")
val REL_LIBS_VERSIONS_TOML = REL_ROOT_FILES + "libs.versions.toml"


//object CommonFileNames {
const val DS_STORE = ".DS_Store"
const val MODULE_INFO_JAVA_NAME = "module-info.java"
const val BUILDSRC_FILE_NAME = "buildSrc"
//}

//object KNCommandKeys {
val OPEN_KEY = "OPEN"
val OPEN_RELATIVE_KEY = "OPEN_REL"
val OPEN_NEAREST_GRADLE_BUILDSCRIPT = "OPEN_NEAREST_GRADLE_BUILDSCRIPT"
val OPEN_NEARST_KOTLIN_DESCENDENT = "OPEN_NEARST_KOTLIN_DESCENDENT"
//}


enum class RootProject {
  /*not adding more yet because I don't want to select from others in KJG*/
  flow, kcomp;

  val folder = IDE_FOLDER + name
}

val DNN_FOLDER = when (thisMachine) {
  NEW_MAC -> IDE_FOLDER + "dnn"
  OLD_MAC -> REGISTERED_FOLDER["todo/science/dnn"]
  WINDOWS -> null
}
val HEP_FOLDER = when (thisMachine) {
  NEW_MAC -> IDE_FOLDER + "hep"
  OLD_MAC -> REGISTERED_FOLDER["todo/science/hep"]
  WINDOWS -> null
}

