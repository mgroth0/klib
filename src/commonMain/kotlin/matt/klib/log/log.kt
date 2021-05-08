package matt.klib.log

var DEBUG = false

fun debug(s: Any) {
  if (DEBUG) {
	println(s.toString())
  }
}

fun warn(s: Any) {
  println("WARNING:${s.toString().uppercase()}")
}