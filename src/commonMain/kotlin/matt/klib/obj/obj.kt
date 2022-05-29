package matt.klib.obj

import matt.klib.log.warn

interface Identified {
  val id: Int
}
interface MaybeIdentified {
  val id: Int?
}

interface Named {
  var name: String

}

open class Unique(
  open var name: String,
  override var id: Int
): Identified {
  override fun toString() =
	"${this::class.simpleName} $id: $name"
}

fun new_id(
  vararg against: List<Identified>
): Int {
  warn("what if i delete the highest? i need to keep a record of nextID instead of this")
  return against.flatMap { it }.maxOf { it.id } + 1
}

interface DSL