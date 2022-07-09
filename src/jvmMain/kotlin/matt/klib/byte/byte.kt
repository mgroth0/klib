package matt.klib.byte

import kotlin.experimental.and

data class ByteSize(val bytes: Long): Comparable<ByteSize> {
  constructor(bytes: Number): this(bytes.toLong())

  companion object {
	const val KILO: Long = 1024
	const val MEGA = KILO*KILO
	const val GIGA = MEGA*KILO
	const val TERA = GIGA*KILO
  }

  val kilo by lazy { bytes.toDouble()/KILO }
  val mega by lazy { bytes.toDouble()/MEGA }
  val giga by lazy { bytes.toDouble()/GIGA }
  val tera by lazy { bytes.toDouble()/TERA }

  val formatted by lazy {
	println("trying to format $bytes")
	when {
	  tera > 1 || tera < -1 -> "%.3f TB".format(tera)
	  giga > 1 || giga < -1 -> "%.3f GB".format(giga)
	  mega > 1 || mega < -1 -> "%.3f MB".format(mega)
	  kilo > 1 || kilo < -1 -> "%.3f KB".format(kilo)
	  else                  -> "$bytes B"
	}
  }

  override fun compareTo(other: ByteSize): Int {
	return this.bytes.compareTo(other.bytes)
  }

  override fun toString(): String {
	return formatted
  }

  operator fun plus(other: ByteSize): ByteSize {
	return ByteSize(bytes + other.bytes)
  }

  operator fun minus(other: ByteSize): ByteSize {
	return ByteSize(bytes - other.bytes)
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