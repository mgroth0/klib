package matt.klib.sys

interface Mac

sealed class Machine(
  val homeDir: String,
  val registeredDir: String,
)

object OLD_MAC: Machine(
  homeDir = "/Users/matt",
  registeredDir = "Desktop/registered",
), Mac

object NEW_MAC: Machine(
  homeDir = "/Users/matthewgroth",
  registeredDir = "registered",
), Mac

sealed interface Windows

object WINDOWS_11_PAR_WORK: Machine(
  homeDir = "C:\\Users\\matthewgroth",
  registeredDir = "registered",
), Windows

object GAMING_WINDOWS: Machine(
  homeDir = ":C::!@#$%^&*(C$^Some/Weird/Windows/Path",
  registeredDir = ":C::!@#$%^&*(C$^Some/Weird/Windows/Path", /*btw, delete .registeredDir file on windows home folder*/
), Windows

interface Linux

object OPEN_MIND: Machine(
  homeDir = "/om2/user/mjgroth",
  registeredDir = "registered",
), Linux
