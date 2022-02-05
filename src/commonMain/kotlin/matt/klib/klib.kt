package matt.klib


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
