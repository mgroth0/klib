@file:JvmName("LangKtJvm")

package matt.klib.lang

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind.AT_LEAST_ONCE
import kotlin.contracts.InvocationKind.EXACTLY_ONCE
import kotlin.contracts.contract
import kotlin.jvm.JvmName
import kotlin.reflect.KClass

infix fun Boolean.ifTrue(op: ()->Unit) {
  if (this) op()
}


infix fun <R> Boolean.ifTrueOrNull(op: ()->R): R? {
  return if (this) op() else null
}

infix fun Boolean.ifFalse(op: ()->Unit) {
  if (!this) op()
}


fun <T: Any> T.inList(): List<T> {
  return listOf(this)
}

fun inlined(op: ()->Unit) {
  //    Pleasework()
  op()
}


/*kinda how JetBrains wants us to do it*/
fun String.cap() =
  replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
/*if I go back to 1.4: this.capitalize()*/


/*kinda how JetBrains wants us to do it*/
fun String.decap() =
  replaceFirstChar { it.lowercase() }
/*if I go back to 1.4: this.decapitalize()*/


val Any.void get() = Unit


infix fun <A, B, C> Pair<A, B>.trip(third: C): Triple<A, B, C> {
  return Triple(first, second, third)
}


@OptIn(ExperimentalContracts::class)
inline fun <T: Any> T.alsoPrintln(op: T.()->String): T {
  contract {
	callsInPlace(op, EXACTLY_ONCE)
  }
  println(op.invoke(this))
  return this
}


@ExperimentalContracts
inline fun <T: Any> T.go(block: (T)->Unit) {
  contract {
	callsInPlace(block, EXACTLY_ONCE)
  }
  block(this)
}


@ExperimentalContracts
inline fun <T: Any> T.applyIt(block: T.(T)->Unit): T {
  contract {
	callsInPlace(block, EXACTLY_ONCE)
  }
  block(this)
  return this
}

fun <T: Any> T.ifIs(other: Any?) = when {
  this != other -> null
  else          -> this
}

fun <T: Any> T.ifIsNot(other: Any?) = when (other) {
  this -> null
  else -> this
}
typealias B = Boolean
typealias S = String
typealias I = Int
typealias D = Double
typealias L = Long
typealias Num = Number


infix fun <T> MutableCollection<T>.setAll(c: Collection<T>) {
  /*when (this) {
    is ObservableList<T>
  }*/
  clear()
  c.forEach { add(it) }
}


fun <E> MutableCollection<E>.addIfNotIn(e: E): Boolean {
  return if (e in this) {
	false
  } else {
	add(e)
	true
  }
}


@ExperimentalContracts
inline fun whileTrue(op: ()->Boolean) {
  contract {
	callsInPlace(op, AT_LEAST_ONCE)
  }
  @Suppress("ControlFlowWithEmptyBody")
  while (op()) {
  }
}


fun err(s: String = ""): Nothing {
  println("demmy")
  throw RuntimeException(s)
}

val NOT_IMPLEMENTED: Nothing get() = throw NotImplementedError()
val NEVER: Nothing get() = err("NEVER")

fun listsEqual(list1: List<*>, list2: List<*>): Boolean {

  if (list1.size != list2.size)
	return false

  val pairList = list1.zip(list2)

  return pairList.all { (elt1, elt2) ->
	elt1 == elt2
  }
}


sealed interface KotlinPrimitive {
  val kls: KClass<*>
}

enum class Ints(override val kls: KClass<*>): KotlinPrimitive {
  Byte(kotlin.Byte::class),
  Short(kotlin.Short::class),
  Int(kotlin.Int::class),
  Long(kotlin.Long::class),
}

enum class IntArrays(override val kls: KClass<*>): KotlinPrimitive {
  ByteArray(kotlin.ByteArray::class),
  IntArray(kotlin.IntArray::class),
  ShortArray(kotlin.ShortArray::class),
  LongArray(kotlin.LongArray::class),
}

enum class FloatingPoints(override val kls: KClass<*>): KotlinPrimitive {
  Float(kotlin.Float::class),
  Double(kotlin.Double::class),
}

enum class FloatingPointArrays(override val kls: KClass<*>): KotlinPrimitive {
  FloatArray(kotlin.FloatArray::class),
  DoubleArray(kotlin.DoubleArray::class),
}

enum class UnsignedInts(override val kls: KClass<*>): KotlinPrimitive {
  UByte(kotlin.UByte::class),
  UShort(kotlin.UShort::class),
  UInt(kotlin.UInt::class),
  ULong(kotlin.ULong::class),
}

@OptIn(ExperimentalUnsignedTypes::class)
enum class UnsignedIntArrays(override val kls: KClass<*>): KotlinPrimitive {
  /*unsigned arrays and ranges*/
  UByteArray(kotlin.UByteArray::class),
  UShortArray(kotlin.UShortArray::class),
  UIntArray(kotlin.UIntArray::class),
  ULongArray(kotlin.ULongArray::class),
}

enum class Booleans(override val kls: KClass<*>): KotlinPrimitive {
  Boolean(kotlin.Boolean::class),
}

enum class BooleanArrays(override val kls: KClass<*>): KotlinPrimitive {
  BooleanArray(kotlin.BooleanArray::class),
}

enum class Chars(override val kls: KClass<*>): KotlinPrimitive {
  Char(kotlin.Char::class),
}

enum class CharArrays(override val kls: KClass<*>): KotlinPrimitive {
  CharArray(kotlin.CharArray::class),
}

enum class Strings(override val kls: KClass<*>): KotlinPrimitive {
  String(kotlin.String::class),
}

enum class Arrays(override val kls: KClass<*>): KotlinPrimitive {
  Array(kotlin.Array::class),
}

inline val <reified T: Any> KClass<T>.jDefault: T?
  get() {
	return when (T::class) {
	  Boolean::class -> false as T
	  Double::class  -> 0.0 as T
	  Float::class   -> 0.0f as T
	  Byte::class    -> 0 as T
	  Int::class     -> 0 as T
	  Long::class    -> 0 as T
	  Short::class   -> 0 as T
	  Char::class    -> '\u0000' as T
	  else           -> null
	}
  }

//@Suppress("UnusedReceiverParameter") val KClass<Boolean>.jDefault: Boolean get() = false
//@Suppress("UnusedReceiverParameter") val KClass<Double>.jDefault: Double get() = 0.0
//@Suppress("UnusedReceiverParameter") val KClass<Byte>.jDefault: Byte get() = 0
//@Suppress("UnusedReceiverParameter") val KClass<Int>.jDefault: Int get() = 0
//@Suppress("UnusedReceiverParameter") val KClass<Long>.jDefault: Long get() = 0
//@Suppress("UnusedReceiverParameter") val KClass<Float>.jDefault: Float get() = 0.0f
//@Suppress("UnusedReceiverParameter") val KClass<Char>.jDefault: Char get() = '\u0000'
//@Suppress("UnusedReceiverParameter") val KClass<Short>.jDefault: Short get() = 0


fun noExceptions(op: ()->Unit): Boolean {
  return try {
	op()
	true
  } catch (e: Exception) {
	false
  }
}

fun <R> nullIfExceptions(op: ()->R): R? {
  return try {
	op()
  } catch (e: Exception) {
	null
  }
}

fun opt(b: Boolean, s: Any): Array<out Any> = if (b) arrayOf(s) else arrayOf()
fun <R> ifOrNull(b: Boolean, op: ()->R) = if (b) op() else null

class If(val b: Boolean) {
  fun then(vararg s: Any?): Array<out Any?> = if (b) s else arrayOf()
}
