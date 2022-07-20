package matt.klib.commons

import matt.klib.lang.err
import matt.klib.sys.GAMING_WINDOWS
import matt.klib.sys.Linux
import matt.klib.sys.Mac
import matt.klib.sys.Machine
import matt.klib.sys.NEW_MAC
import matt.klib.sys.OLD_MAC
import matt.klib.sys.OPEN_MIND
import matt.klib.sys.WINDOWS_11_PAR_WORK
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.contracts.InvocationKind.AT_MOST_ONCE
import kotlin.contracts.contract
import kotlin.io.path.Path
import kotlin.io.path.exists

private val WINDOWS_CMD_BASH_PREFIX = arrayOf("C:\\Program Files (x86)\\Git\\bin\\bash.exe", "-c")
fun wrapWindowsBashCmd(vararg command: String) = arrayOf(*WINDOWS_CMD_BASH_PREFIX, command.joinToString(" "))

val os: String by lazy { System.getProperty("os.name") }
val uname by lazy {
  val proc = if ("Windows" in os) ProcessBuilder(*WINDOWS_CMD_BASH_PREFIX)
  else ProcessBuilder()



  proc.command() += listOf(
	listOf(
	  "/usr/bin/uname",
	  "/bin/uname" /*vagrant, singularity*/
	).first { Path(it).exists() },
	"-m"
  )

  BufferedReader(InputStreamReader(proc.start().inputStream)).readText().trim()
}
val userName: String by lazy { System.getProperty("user.name") }
val thisMachine: Machine by lazy {
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


const val GITHUB_USERNAME = "mgroth0"
val SYLABS_UNAME = GITHUB_USERNAME
const val DEFAULT_GITHUB_BRANCH_NAME = "master"


inline fun onLinux(op: ()->Unit) {
  contract {
	callsInPlace(op, AT_MOST_ONCE)
  }
  if (thisMachine is Linux) op()
}

inline fun onMac(op: ()->Unit) {
  contract {
	callsInPlace(op, AT_MOST_ONCE)
  }
  if (thisMachine is Mac) op()
}
