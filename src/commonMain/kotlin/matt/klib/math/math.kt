package matt.klib.math

import kotlinx.serialization.Serializable
import matt.klib.lang.setAll
import matt.klib.math.PointDim.X
import matt.klib.math.PointDim.Y
import kotlin.jvm.JvmName
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.sqrt
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


@Serializable
data class BasicPoint(
  override val x: Double, override val y: Double
): Point {
  constructor(x: Number, y: Number): this(x = x.toDouble(), y = y.toDouble())

  fun normDist(other: BasicPoint) = sqrt((x - other.x).sq() + (y - other.y).sq())

  override val xDouble get() = x
  override val yDouble get() = y
  override fun clone(newX: Number?, newY: Number?): Point {
	return copy(x = newX?.toDouble() ?: x, y = newY?.toDouble() ?: y)
  }

  override fun toBasicPoint(): BasicPoint {
	return this
  }
}


enum class PointDim { X, Y }

interface Point {
  fun getDim(dim: PointDim) = when (dim) {
	X -> x
	Y -> y
  }

  fun getDimDouble(dim: PointDim) = when (dim) {
	X -> xDouble
	Y -> yDouble
  }

  fun cloneWithNewDim(
	dim: PointDim, newValue: Double
  ): Point {
	return when (dim) {
	  X -> clone(newX = newValue)
	  Y -> clone(newY = newValue)
	}
  }


  val x: Any
  val y: Any

  val xDouble: Double
  val yDouble: Double

  //  val xDouble
  //	get() = when (this) {
  //
  //	}
  //
  //  val yDouble
  //	get() = when (this) {
  //	  is JsonPoint  -> y
  //	  is matt.kjlib.jmath.point.BasicPoint -> y
  //	  is matt.kjlib.jmath.point.APoint     -> y.toDouble()
  //	}


  fun clone(
	newX: Number? = null, newY: Number? = null
  ): Point

  fun toBasicPoint(): BasicPoint
}


val Collection<Point>.trough get() = minByOrNull { it.yDouble }
val Collection<Point>.gradient get() = (maxOf { it.yDouble } - minOf { it.yDouble })/(maxOf { it.xDouble } - minOf { it.xDouble })

fun List<Point>.derivative(n: Int = 1): List<Point> {/*could make this recursive but functionally equivalent*/
  require(n > -1)
  if (n == 0) return this
  var d = this
  repeat((1..n).count()) {
	d = if (d.size < 2) emptyList()
	else d.subList(1, d.size).mapIndexed { index, point ->
	  BasicPoint(x = point.xDouble, y = point.yDouble - d[index].yDouble)
	}
  }
  return d
}

fun List<Point>.normalizeToMax(dim: PointDim): List<Point> {
  val max = maxOf { it.getDimDouble(dim) }
  return map { it.cloneWithNewDim(dim = dim, newValue = it.getDimDouble(dim)/max*100.0) }
}

fun List<Point>.normalizeToMinMax(dim: PointDim): List<Point> {
  val min = minOf { it.getDimDouble(dim) }
  val max = maxOf { it.getDimDouble(dim) } - min
  return map { it.cloneWithNewDim(dim = dim, newValue = (it.getDimDouble(dim) - min)/max*100.0) }
}


fun List<Point>.showAsPercent(dim: PointDim): List<Point> {
  return map {
	val newValue = it.getDimDouble(dim)*100
	it.cloneWithNewDim(dim = dim, newValue = newValue)
  }
}

fun Iterable<MutableList<Point>>.maxByTroughY() = maxByOrNull { it.trough!!.yDouble }!!
fun Iterable<MutableList<Point>>.minByTroughY() = minByOrNull { it.trough!!.yDouble }!!

fun Iterable<MutableList<Point>>.shiftAllByTroughs() {
  val higherTrough = maxByTroughY()
  val lowerTrough = minByTroughY()
  filter { it != lowerTrough }.forEach {
	it.setAll(
	  higherTrough.map { it.clone(newY = it.yDouble - (higherTrough.trough!!.yDouble - lowerTrough.trough!!.yDouble)) })
  }
}

fun List<Point>.toBasicPoints() = map { it.toBasicPoint() }

