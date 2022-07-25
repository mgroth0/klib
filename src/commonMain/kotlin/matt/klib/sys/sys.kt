package matt.klib.sys

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
  val homeDir: String,
  val registeredDir: String,
): OS

object OLD_MAC: Machine(
  homeDir = "/Users/matt",
  registeredDir = "Desktop/registered",
), Mac

object NEW_MAC: Machine(
  homeDir = "/Users/matthewgroth",
  registeredDir = "registered",
), Mac

sealed interface Windows: OS {
  override val caseSensitive get() = false
  override val pathSep get() = "\\"
  override val wrongPathSep get() = "/"
}

object WINDOWS_11_PAR_WORK: Machine(
  homeDir = "C:\\Users\\matthewgroth",
  registeredDir = "registered",
), Windows

object GAMING_WINDOWS: Machine(
  homeDir = ":C::!@#$%^&*(C$^Some/Weird/Windows/Path",
  registeredDir = ":C::!@#$%^&*(C$^Some/Weird/Windows/Path", /*btw, delete .registeredDir file on windows home folder*/
), Windows

sealed interface Linux: Unix {
  override val caseSensitive get() = true
}

object OPEN_MIND: Machine(
  homeDir = "/om2/user/mjgroth",
  registeredDir = "registered",
), Linux
