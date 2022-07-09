package matt.klib.byte

import matt.klib.byte.ByteSize.ByteUnit
import matt.klib.byte.ByteSize.ByteUnit.B
import matt.klib.byte.ByteSize.ByteUnit.GB
import matt.klib.byte.ByteSize.ByteUnit.KB
import matt.klib.byte.ByteSize.ByteUnit.MB
import matt.klib.byte.ByteSize.ByteUnit.TB
import matt.klib.dmap.withStoringDefault
import kotlin.experimental.and

data class ByteSize(val bytes: Long): Comparable<ByteSize> {
  constructor(bytes: Number): this(bytes.toLong())

  val unitReps = mutableMapOf<ByteUnit, Double>().withStoringDefault {
	bytes.toDouble()/it.size
  }

  enum class ByteUnit(val size: Long) {
	B(1), KB(1024), MB(KB.size*KB.size), GB(MB.size*KB.size), TB(GB.size*KB.size)
  }

  val b get() = unitReps[B]
  val kb get() = unitReps[KB]
  val mb get() = unitReps[MB]
  val gb get() = unitReps[GB]
  val tb get() = unitReps[TB]

  val bestUnit by lazy {
	ByteUnit.values().reversed().firstOrNull {
	  val rep = unitReps[it]
	  rep > 1 || rep < -1
	} ?: B
  }

  val formatted by lazy { FormattedByteSize(unitReps[bestUnit], bestUnit) }
  override fun compareTo(other: ByteSize) = this.bytes.compareTo(other.bytes)
  override fun toString() = formatted.toString()
  operator fun plus(other: ByteSize) = ByteSize(bytes + other.bytes)
  operator fun minus(other: ByteSize) = ByteSize(bytes - other.bytes)
}

class FormattedByteSize(val num: Double, val unit: ByteUnit) {
  override fun toString(): String {
	return if (unit == B) "$num ${unit.name}"
	else "%.3f ${unit.name}".format(num)
  }
}


@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@JvmName("sumOfByteSize")
inline fun <T> Iterable<T>.sumOf(selector: (T)->ByteSize): ByteSize {
  var sum: ByteSize = ByteSize(0)
  for (element in this) {
	sum += selector(element)
  }
  return sum
}

private val hexArray = "0123456789ABCDEF".toCharArray()


@Suppress("unused")
fun ByteArray.toHex(): String {
  val hexChars = CharArray(size*2)
  for (j in indices) {
	val v = (this[j] and 0xFF.toByte()).toInt()

	hexChars[j*2] = hexArray[v ushr 4]
	hexChars[j*2 + 1] = hexArray[v and 0x0F]
  }
  return String(hexChars)
}