package matt.klib.sys

import matt.klib.lang.NOT_IMPLEMENTED
import matt.klib.lang.err
import matt.klib.sys.OpenMindNode.OpenMindMainHeadNode

sealed interface OS {
  val caseSensitive: Boolean
  val pathSep: String
  val wrongPathSep: String
}

sealed interface Unix: OS {
  override val pathSep get() = "/"
  override val wrongPathSep get() = "\\"
}

sealed interface Mac: Unix {
  override val caseSensitive get() = false
}

sealed class Machine(
  getHomeDir: Machine.()->String,
  getRegisteredDir: Machine.()->String,
): OS {
  val homeDir by lazy { getHomeDir() }
  val registeredDir by lazy { getRegisteredDir() }
}

sealed class MacMachine(
  homeDir: String,
  registeredDir: String,
): Machine(
  getHomeDir = { homeDir },
  getRegisteredDir = { registeredDir }
), Mac

object OLD_MAC: MacMachine(
  homeDir = "/Users/matt",
  registeredDir = "Desktop/registered",
)

object NEW_MAC: MacMachine(
  homeDir = "/Users/matthewgroth",
  registeredDir = "registered",
)

sealed interface Windows: OS {
  override val caseSensitive get() = false
  override val pathSep get() = "\\"
  override val wrongPathSep get() = "/"
}

sealed class WindowsMachine(
  getHomeDir: Machine.()->String,
  getRegisteredDir: Machine.()->String,
): Machine(
  getHomeDir = getHomeDir,
  getRegisteredDir = getRegisteredDir
), Windows

object WINDOWS_11_PAR_WORK: WindowsMachine(
  getHomeDir = { "C:\\Users\\matthewgroth" },
  getRegisteredDir = { "registered" },
)

object GAMING_WINDOWS: WindowsMachine(
  getHomeDir = { err("idk what the homedir of gaming windows is") },
  getRegisteredDir = {
	err(
	  "idk what the registered dir of gaming windows is, btw, delete .registeredDir file on windows home folder"
	)
  }
)

class UnknownWindowsMachine(): WindowsMachine(
  getHomeDir = { "idk what the home dir of $this is" },
  getRegisteredDir = { "idk what the registered dir of $this is" },
)


sealed interface Linux: Unix {
  override val caseSensitive get() = true
}

sealed class LinuxMachine(
  getHomeDir: Machine.()->String,
  getRegisteredDir: Machine.()->String,
): Machine(
  getHomeDir = getHomeDir,
  getRegisteredDir = getRegisteredDir
), Linux

class OpenMind(
  val node: OpenMindNode,
  val sImgLoc: String?,
  slurmJobID: String?
): LinuxMachine(
  getHomeDir = { "/om2/user/mjgroth" },
  getRegisteredDir = { "registered" },
) {
  val inSingularity = sImgLoc != null
  val slurmJobID = slurmJobID?.toInt()
  val inSlurmJob = slurmJobID != null
}

enum class OpenMindNode {
  Polestar,
  OpenMindDTN,
  OpenMindMainHeadNode /*7*/
}

//object MainMachineForKcompRemoteExecution: OpenMind(
//  node = OpenMindMainHeadNode,
//  sImgLoc =
//)

class VagrantLinuxMachine: LinuxMachine(
  getHomeDir = { NOT_IMPLEMENTED },
  getRegisteredDir = { NOT_IMPLEMENTED },
)

class UnknownLinuxMachine(val hostname: String): LinuxMachine(
  getHomeDir = { "idk what the home dir of $this is" },
  getRegisteredDir = { "idk what the registered dir of $this is" },
) {
  override fun toString() = "[${UnknownLinuxMachine::class.simpleName} with hostname=$hostname]"
}
