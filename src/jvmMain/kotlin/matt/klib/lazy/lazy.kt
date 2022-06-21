@file:Suppress("ClassName")

package matt.klib.lazy

import kotlin.reflect.KProperty

fun <A, T> lazyWithArg(initializer: (A)->T): LazyWithArg<A, T> = SynchronizedLazyWithInitializerArg(initializer)

interface LazyWithArg<A, T> {
  fun getValueWithArg(a: A): T
  operator fun getValue(thisRef: Any?, property: KProperty<*>): (A)->T = { getValueWithArg(it) }
}

private class SynchronizedLazyWithInitializerArg<A, T>(initializer: (A)->T): LazyWithArg<A, T> {

  companion object {
	private object MY_UNINITIALIZED_VALUE
  }

  private var initializer: ((A)->T)? = initializer
  @Volatile private var _value: Any? = MY_UNINITIALIZED_VALUE
  private val lock = this

  override fun getValueWithArg(a: A): T {
	val v1 = _value
	if (v1 !== MY_UNINITIALIZED_VALUE) {
	  @Suppress("UNCHECKED_CAST")
	  return v1 as T
	}
	return synchronized(lock) {
	  val v2 = _value
	  if (v2 !== MY_UNINITIALIZED_VALUE) {
		@Suppress("UNCHECKED_CAST") (v2 as T)
	  } else {
		val typedValue = initializer!!(a)
		_value = typedValue
		initializer = null
		typedValue
	  }
	}
  }
}

fun <A, T> lazyWithReciever(initializer: (A)->T): LazyWithReceiver<A, T> = SynchronizedLazyWithInitializerReceiver(initializer)

interface LazyWithReceiver<A, T> {
  fun getValueWithReceiver(a: A): T
  operator fun getValue(thisRef: A?, property: KProperty<*>): T = getValueWithReceiver(thisRef as A)
}

private class SynchronizedLazyWithInitializerReceiver<A, T>(initializer: (A)->T): LazyWithReceiver<A, T> {

  companion object {
	private object MY_UNINITIALIZED_VALUE
  }

  private var initializer: ((A)->T)? = initializer
  @Volatile private var _value: Any? = MY_UNINITIALIZED_VALUE
  private val lock = this

  override fun getValueWithReceiver(a: A): T {
	val v1 = _value
	if (v1 !== MY_UNINITIALIZED_VALUE) {
	  @Suppress("UNCHECKED_CAST")
	  return v1 as T
	}
	return synchronized(lock) {
	  val v2 = _value
	  if (v2 !== MY_UNINITIALIZED_VALUE) {
		@Suppress("UNCHECKED_CAST") (v2 as T)
	  } else {
		val typedValue = initializer!!(a)
		_value = typedValue
		initializer = null
		typedValue
	  }
	}
  }
}




