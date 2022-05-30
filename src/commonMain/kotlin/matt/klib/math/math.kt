package matt.klib.math

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.Random.Default


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


fun List<Float>.mean() = sum()/size
fun FloatArray.mean() = sum()/size
fun List<Double>.mean() = sum()/size
fun Sequence<Double>.mean() = toList().mean()

@JvmName("meanInt")
fun Sequence<Int>.mean() = map { it.toDouble() }.mean()

fun List<Double>.median() = if (this.size == 0) null else this.sorted()[round(this.size/2.0).toInt() - 1]
fun DoubleArray.mean() = sum()/size
fun IntArray.intMean() = (sum()/size.toDouble()).roundToInt()
fun IntArray.doubleMean() = (sum()/size.toDouble())


fun orth(degrees: Float): Float {
  require(degrees in 0.0f..180.0f)
  return if (degrees < 90.0f) degrees + 90.0f
  else degrees - 90.0f
}

fun orth(degrees: Double): Double {
  require(degrees in 0.0..180.0)
  return if (degrees < 90.0) degrees + 90.0
  else degrees - 90.0
}

fun <T> Iterable<T>.meanOf(op: (T)->Double) = map { op(it) }.mean()


const val DOUBLE_ONE = 1.0

fun List<Float>.logSum() = fold(0f) { acc, d ->
  acc + ln(d)
}

fun List<Double>.logSum() = fold(0.0) { acc, d ->
  acc + ln(d)
}


infix fun FloatArray.dot(other: FloatArray): Float {
  require(this.size == other.size)
  var ee = 0.0.toFloat()
  (0 until this.size).forEach { x ->
	val first = this[x]
	val second = other[x]
	if (!first.isNaN() && !second.isNaN()) {
	  val r = this[x]*other[x]
	  ee += r
	}
  }
  return ee
}

infix fun DoubleArray.dot(other: DoubleArray): Double {
  require(this.size == other.size)
  var ee = 0.0
  (0 until this.size).forEach { x ->
	val first = this[x]
	val second = other[x]
	if (!first.isNaN() && !second.isNaN()) {
	  val r = this[x]*other[x]
	  ee += r
	}
  }
  return ee

}

enum class UnitType(val symbol: String?, val longNameSingular: String?, val longNamePlural: String?) {
  PERCENT("%", "percent", "percent"),
  DEGREES("°", "degree", "degrees"),
  RATIO(null, null, null)
}

data class Sides(val adj: Double, val opp: Double)

@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@kotlin.jvm.JvmName("sumOfFloat")
public inline fun <T> Iterable<T>.sumOf(selector: (T)->Float): Float {
  var sum: Float = 0f
  for (element in this) {
	sum += selector(element)
  }
  return sum
}

fun randomAngleInDegrees() = Random.nextDouble()*360.0

/**
 * Shortest distance (angular) between two angles.
 * It will be in range [0, 180].
 */
fun angularDifference(alpha: Double, beta: Double): Double {
  val phi = abs(beta - alpha)%360.0  /*This is either the distance or 360 - distance*/
  return if (phi > 180.0) 360.0 - phi else phi
}

fun nextUnitDouble() = Default.nextDouble()*2 - 1

infix fun Array<out Float?>.dot(other: Array<out Float?>): Float {
  require(this.size == other.size)
  var ee = 0.0f
  (0 until this.size).forEach { x ->
	val first = this[x]
	val second = other[x]
	if (first != null && second != null) {
	  val r = first*second
	  ee += r
	}
  }
  return ee

}

fun Sequence<Double>.geometricMean(): Double = toList().geometricMean()
fun List<Double>.geometricMean(bump: Double = 1.0) = fold(1.0) { acc, d ->
  acc*d*bump
}.pow(DOUBLE_ONE/size)


/*log (5*4*3*2*1) = log (5) + log(4) ...*/
fun Int.logFactorial(): Double {
  require(this > -1)
  if (this == 0) return 0.0
  var i = this
  var r = 0.0
  while (i > 0) {
	r += ln(i.toDouble())
	i -= 1
  }
  return r
}

fun Int.logFactorialFloat(): Float {
  require(this > -1)
  if (this == 0) return 0.0.toFloat()
  var i = this
  var r = 0.0.toFloat()
  while (i > 0) {
	r += ln(i.toFloat())
	i -= 1
  }
  return r
}


fun Float.getPoisson(): Int {
  /*val lambda = this*/
  val L = exp(-this)
  var p = 1.0f
  var k = 0
  do {
	k++
	p *= Default.nextFloat()
  } while (p > L)
  return (k - 1)
}

/*https://stackoverflow.com/questions/1241555/algorithm-to-generate-poisson-and-binomial-random-numbers*/
fun Double.getPoisson(): Int {
  /*val lambda = this*/
  val L = exp(-this)
  var p = 1.0
  var k = 0
  do {
	k++
	p *= Default.nextDouble()
  } while (p > L)
  return (k - 1)


}



@Suppress("unused")
fun Double.roundToDecimal(n: Int): Double {

  val temp = this*(n*10)
  val tempInt = temp.roundToInt().toDouble()
  return tempInt/(n*10)

}



fun Double.floorInt() = floor(this).toInt()