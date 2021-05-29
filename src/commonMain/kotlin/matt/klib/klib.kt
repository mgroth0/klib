package matt.klib


infix fun <K, V> Map<K, V>.isEquivalentTo(other: Map<K, V>?): Boolean {
//  println("here1")
  if (other == null) return false
//  println("here2")
  if (this.keys.size != other.keys.size) return false
//  println("here3")
  this.forEach { (k, v) ->
	if (k !in other) return false
//	println("here4")
	if (v != other[k]) return false
//	println("here5")
  }
//  println("here6")
  return true
}


interface Searchable {
  val searchSeq: Sequence<String>
}

