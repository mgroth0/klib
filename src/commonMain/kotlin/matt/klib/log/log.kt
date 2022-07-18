package matt.klib.log

var DEBUG = false
var PROFILE = true

fun debug(s: Any) {
  if (DEBUG) {
	println(s.toString())
  }
}

fun profile(s: Any) {
  if (PROFILE) {
	println(s.toString())
  }
}

val warned = mutableSetOf<Any>()
fun warnIf(b: Boolean, w: ()->String) {
  if (!b) warn(w())
}

fun warn(vararg s: Any) {
  s.forEach {
	warned += it
	println("WARNING:${it.toString().uppercase()}")
  }
}

fun warnOnce(s: Any) {
  if (s in warned) return
  else {
	warn(s)
	warned += s
	if (warned.size > 100) {
	  throw RuntimeException("too many warnings")
	}
  }
}