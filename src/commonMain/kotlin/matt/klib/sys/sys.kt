package matt.klib.sys

interface Mac

sealed class Machine(
  val homeDir: String,
  val registeredDir: String,
  val flowFolder: String?
)

object OLD_MAC: Machine(
  homeDir = "/Users/matt",
  registeredDir = "Desktop/registered",
  flowFolder = "todo/flow",
), Mac

object NEW_MAC: Machine(
  homeDir = "/Users/matthewgroth",
  registeredDir = "registered",
  flowFolder = "ide/flow"
), Mac

object WINDOWS: Machine(
  homeDir = ":C::!@#$%^&*(C$^Some/Weird/Windows/Path",
  registeredDir = ":C::!@#$%^&*(C$^Some/Weird/Windows/Path", /*btw, delete .registeredDir file on windows home folder*/
  flowFolder = null
)

