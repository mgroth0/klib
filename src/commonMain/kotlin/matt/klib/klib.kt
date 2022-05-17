@file:OptIn(ExperimentalContracts::class)

package matt.klib

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

interface CommandWithExitStatus {
  fun run(arg: String): ExitStatus
}

inline fun <T> T.takeUnlessPrintln(msg: String, predicate: (T) -> Boolean): T? {
  contract {
	callsInPlace(predicate, EXACTLY_ONCE)
  }
  return if (!predicate(this)) this else run{
	println(msg)
	null
  }
}
