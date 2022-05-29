package matt.klib.math

import kotlinx.serialization.Serializable
import kotlin.math.abs
import kotlin.math.pow


const val THOUSAND = 1000.0
const val MILLION = THOUSAND*THOUSAND
const val BILLION = THOUSAND*MILLION
const val TRILLION = THOUSAND*BILLION
const val QUADRILLION = THOUSAND*TRILLION

fun Int.isEven() = this == 0 || abs(this).mod(2) == 0
fun Int.isOdd() = !isEven()

fun Float.sq() = pow(2)
fun Float.cubed() = pow(3)

fun Double.sq() = pow(2)
fun Double.cubed() = pow(3)


fun DoubleArray.toIntArray() = this.map { it.toInt() }.toIntArray()

@Serializable
data class Geometry(
  val x: Double,
  val y: Double,
  val width: Double,
  val height: Double
)

operator fun Number.unaryMinus(): Number {
  return when (this) {
	is Double -> -this
	is Int    -> -this
	is Long   -> -this
	is Short  -> -this
	is Float  -> -this
	else      -> throw RuntimeException("how to unary minus ${this}?")
  }
}
