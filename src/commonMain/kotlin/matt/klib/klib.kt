@file:OptIn(ExperimentalContracts::class, ExperimentalContracts::class)

package matt.klib

import matt.klib.lang.cap
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind.EXACTLY_ONCE
import kotlin.contracts.contract



infix fun <K, V> Map<K, V>.isEquivalentTo(other: Map<K, V>?): Boolean {
  if (other == null) return false
  if (this.keys.size != other.keys.size) return false
  this.forEach { (k, v) ->
	if (k !in other) return false
	if (v != other[k]) return false
  }
  return true
}


interface Searchable {
  val searchSeq: Sequence<String>
}

interface Command {
  fun run(arg: String)
}

enum class ExitStatus {
  CONTINUE, EXIT
}

//interface SingleArgCommandWithExitStatus {
//  fun run(arg: String): ExitStatus
//}
//interface SingleArgCommandWithStringResult {
//  fun run(arg: String): String
//}

inline fun <T> T.takeUnlessPrintln(msg: String, predicate: (T)->Boolean): T? {
  contract {
	callsInPlace(predicate, EXACTLY_ONCE)
  }
  return if (!predicate(this)) this else run {
	println(msg)
	null
  }
}


fun helloKlib(): String = "hello from klib commons"




val todos = mutableSetOf<String>()
fun todo(vararg s: String) {
  s.forEach {
	todos += it
	println("todo: $it")
  }
}

class Path(path: String) {
  val path = path.removePrefix("/").removeSuffix("/")
  operator fun plus(other: Path) = Path(path + "/" + other.path)
  operator fun plus(other: String) = this + Path(other)
  override fun toString() = path
}


fun <T> analyzeExceptions(op: ()->T): T {
  try {
	return op()
  } catch (e: Throwable) {
	println("${e::class.simpleName} message: ${e.message}")
	e.printStackTrace()
	throw e
  }
}


val MINUTE_MS: Int = 60*1000
val HOUR_MS: Int = 3600*1000

fun IntRange.loop() = toList().loop()
fun <T> List<T>.loop() = Loop(this)


class Loop<T>(private val list: List<T>): Iterable<T> {

  override fun iterator() = object: ListIterator<T> {
	var lastIndex: Int? = null
	override fun hasNext() = list.isNotEmpty()
	override fun hasPrevious() = list.isNotEmpty()
	override fun next(): T {
	  lastIndex = nextIndex()
	  return list[lastIndex!!]
	}

	override fun nextIndex() = when {
	  list.isEmpty()             -> throw NoSuchElementException()
	  lastIndex == null          -> 0
	  lastIndex == list.size - 1 -> 0
	  else                       -> lastIndex!! + 1
	}

	override fun previous(): T {
	  lastIndex = previousIndex()
	  return list[lastIndex!!]
	}

	override fun previousIndex() = when {
	  list.isEmpty()    -> throw NoSuchElementException()
	  lastIndex == null -> list.size - 1
	  lastIndex == 0    -> list.size - 1
	  else              -> lastIndex!! - 1
	}
  }
}





fun <T> Iterator<T>.first(op: (T)->Boolean): T {
  while (hasNext()) {
	val n = next()
	if (op(n)) return n
  }
  throw NoSuchElementException("couldn't find one")
}

fun <T> ListIterator<T>.firstBackwards(op: (T)->Boolean): T {
  while (hasPrevious()) {
	val n = previous()
	if (op(n)) return n
  }
  throw NoSuchElementException("couldn't find one")
}


operator fun String.times(n: Int): String {
  require(n >= 0)
  if (n == 0) return ""
  var r = ""
  repeat(n) {
	r += this
  }
  return r
}



fun <T> Iterator<T>.nextOrNull() = takeIf { hasNext() }?.next()




fun String.hyphonizedToCamelCase() = when {
  "-" !in this -> this
  else         -> split("-").let {
	var r = ""
	it.forEachIndexed { index, s ->
	  if (index == 0) r += s
	  else r += s.cap()
	}
	r
  }
}

data class PixelIndex(val x: Int, val y: Int)

private val computeOnceCache = mutableMapOf<Any, Any?>()
fun <R> computeOnce(key: Any, op: ()->R): R {
  return computeOnceCache[key]?.let {
	@Suppress("UNCHECKED_CAST")
	it as R
  } ?: op().also { computeOnceCache[key] = it }
}
