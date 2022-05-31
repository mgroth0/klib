package matt.klib.sys

enum class Machine(
  val homeDir: String,
  val registeredDir: String,
  val flowFolder: String?
) {
  OLD_MAC(
	homeDir = "/Users/matt",
	registeredDir = "Desktop/registered",
	flowFolder = "todo/flow"
  ),
  NEW_MAC(
	homeDir = "/Users/matthewgroth",
	registeredDir = "registered",
	flowFolder = "ide/flow"
  ),
  WINDOWS(
	homeDir = ":C::!@#$%^&*(C$^Some/Weird/Windows/Path",
	registeredDir = ":C::!@#$%^&*(C$^Some/Weird/Windows/Path", /*btw, delete .registeredDir file on windows home folder*/
	flowFolder = null
  )
}

