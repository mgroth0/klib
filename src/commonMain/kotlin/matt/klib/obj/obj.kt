package matt.klib.obj

import matt.klib.log.warn

interface Identified: MaybeIdentified {
  override val id: Int
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

fun newId(
  vararg against: List<Identified>
): Int {
  warn("what if i delete the highest? i need to keep a record of nextID instead of this")
  return against.flatMap { it }.maxOf { it.id } + 1
}
fun new_id(
  vararg against: List<MaybeIdentified>
): Int {
  warn("what if i delete the highest? i need to keep a record of nextID instead of this")
  return against.flatMap { it }.maxOf { it.id!! } + 1
}

interface DSL


abstract class SimpleData(private val identity: Any) {
  override fun equals(other: Any?): Boolean {
    return other != null && other::class == this::class && (other as SimpleData).identity == identity
  }

  override fun hashCode(): Int {
    return identity.hashCode()
  }
}