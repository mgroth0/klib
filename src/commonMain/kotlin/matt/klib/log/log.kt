package matt.klib.log

var DEBUG = false

fun debug(s: Any) {
  if (DEBUG) {
	println(s.toString())
  }
}

private val warned = mutableListOf<Any>()
fun warn(s: Any) {
  println("WARNING:${s.toString().uppercase()}")
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