package matt.klib.stream

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.InvocationKind.AT_LEAST_ONCE
import kotlin.contracts.InvocationKind.UNKNOWN
import kotlin.contracts.contract

fun <T> Iterable<T>.applyEach(op: T.()->Unit) = forEach { it.apply(op) }

fun <T> Sequence<T>.onEachApply(op: T.()->Unit) = onEach { it.apply(op) }




@ExperimentalContracts
fun <T: Any, R> T.search(
  getTarget: T.()->R?,
  getNext: T.()->T?
): R? {
  contract {
	callsInPlace(getTarget, AT_LEAST_ONCE)
	callsInPlace(getNext, UNKNOWN)
  }
  var next: T? = this
  do {
	next!!.getTarget()?.let {
	  return it
	}
	next = next.getNext()
  } while (next != null)
  return null
}


@ExperimentalContracts
fun <T: Any> T.searchDepth(
  getNext: T.()->T?
): Int {
  contract {
	callsInPlace(getNext, InvocationKind.AT_LEAST_ONCE)
  }
  var i = -1
  var next: T? = this
  do {
	next = next!!.getNext()
	i++
  } while (next != null)
  return i
}






fun <E> Collection<E>.allUnique(): Boolean {
  when (this) {
	is List<E> -> {
	  forEachIndexed { index1, t1 ->
		for (t2 in subList(index1 + 1, size)) {
		  if (t1 == t2) {
			println("t1 is t2")
			println("t1:$t1")
			println("t2:$t2")
			println("index1:$index1")
			println("indexOf(t1)=${this.indexOf(t1)}")
			println("indexOf(t2)=${this.indexOf(t2)}")
			return false
		  }
		}
	  }
	  return true
	}
	is Set<E>  -> {
	  return true
	}
	else       -> return toList().allUnique()
  }
}

fun <E> Collection<E>.allSame(): Boolean {
  if (this.size <= 1) {
	return true
  } else {
	val example = this.first()
	return this.all { it == example }
  }
}
