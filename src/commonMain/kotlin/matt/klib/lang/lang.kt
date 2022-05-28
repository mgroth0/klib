package matt.klib.lang

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.InvocationKind.AT_LEAST_ONCE
import kotlin.contracts.InvocationKind.EXACTLY_ONCE
import kotlin.contracts.contract

infix fun Boolean.ifTrue(op: () -> Unit) {
    if (this) op()
}


infix fun <R> Boolean.ifTrueOrNull(op: () -> R): R? {
   return  if (this) op() else null
}

infix fun Boolean.ifFalse(op: () -> Unit) {
    if (!this) op()
}


fun <T : Any> T.inList(): List<T> {
    return listOf(this)
}

fun inlined(op: () -> Unit) {
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
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  block(this)
}


@ExperimentalContracts
inline fun <T: Any> T.applyIt(block: T.(T)->Unit): T {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
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

val NEVER: Nothing get() = err("NEVER")