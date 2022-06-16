package matt.klib.olist

import matt.klib.lang.err


fun <E> Collection<E>.toBasicObservableList(): BasicObservableList<E> {
  return BasicObservableList(this)
}

fun <E> Iterable<E>.toBasicObservableList(): BasicObservableList<E> {
  return BasicObservableList(this.toList())
}

fun <E> Sequence<E>.toBasicObservableList(): BasicObservableList<E> {
  return BasicObservableList(this.toList())
}

interface MObservable<T> {
  fun onChange(listener: (T)->Unit): (T)->Unit
  fun onChangeUntil(until: (T)->Boolean, listener: (T)->Unit)
  fun onChangeOnce(listener: (T)->Unit) = onChangeUntil({ true }, listener)
}

sealed interface CollectionChange<E>
class Addition<E>(val added: E): CollectionChange<E>
class MultiAddition<E>(val added: Collection<E>): CollectionChange<E>
class Removal<E>(val removed: E): CollectionChange<E>
class MultiRemoval<E>(val removed: Collection<E>): CollectionChange<E>
class Replacement<E>(val removed: E, val added: E): CollectionChange<E>
class Clear<E>: CollectionChange<E>

open class MIteratorWithSomeMemory<E>(
  protected open val itr: MutableIterator<E>,
): MutableIterator<E> by itr {
  protected var hadFirstReturn = false

  protected var _lastReturned: E? = null

  @Suppress("UNCHECKED_CAST") val lastReturned: E
	get() = if (!hadFirstReturn) err("hasn't returned yet") else _lastReturned as E

  override fun next(): E {
	return itr.next().also {
	  hadFirstReturn = true
	  _lastReturned = it
	}
  }
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
open class MListIteratorWithSomeMemory<E>(
  override val itr: MutableListIterator<E>,
): MIteratorWithSomeMemory<E>(itr), MutableListIterator<E> by itr {
  override fun previous(): E {
	return itr.previous().also {
	  hadFirstReturn = true
	  _lastReturned = it
	}
  }
}

abstract class BasicObservableCollection<E>: MObservable<CollectionChange<E>>, Collection<E> {
  private val listeners = mutableListOf<(CollectionChange<E>)->Unit>()
  override fun onChange(listener: (CollectionChange<E>)->Unit): (CollectionChange<E>)->Unit {
	listeners.add(listener)
	return listener
  }

  protected fun change(change: CollectionChange<E>) {
	listeners.forEach { it(change) }
  }

  override fun onChangeUntil(until: (CollectionChange<E>)->Boolean, listener: (CollectionChange<E>)->Unit) {
	var realListener: ((CollectionChange<E>)->Unit)? = null
	realListener = { t: CollectionChange<E> ->
	  listener(t)
	  if (until(t)) listeners -= realListener!!
	}
	listeners += realListener
  }
}

class BasicObservableList<E>(c: Collection<E> = mutableListOf()): BasicObservableCollection<E>(), MutableList<E> {

  private val list = c.toMutableList()


  override val size: Int
	get() = list.size

  override fun contains(element: E): Boolean {
	return list.contains(element)
  }

  override fun containsAll(elements: Collection<E>): Boolean {
	return list.containsAll(elements)
  }

  override fun get(index: Int): E {
	return list[index]
  }

  override fun indexOf(element: E): Int {
	return list.indexOf(element)
  }

  override fun isEmpty(): Boolean {
	return list.isEmpty()
  }

  override fun iterator(): MutableIterator<E> = listIterator()

  override fun lastIndexOf(element: E): Int {
	return list.lastIndexOf(element)
  }

  override fun add(element: E): Boolean {
	val b = list.add(element)
	require(b)
	if (b) {
	  change(Addition(element))
	}
	return b
  }

  override fun add(index: Int, element: E) {
	list.add(index, element)
	change(Addition(element))
  }

  override fun addAll(index: Int, elements: Collection<E>): Boolean {
	val b = list.addAll(index, elements)
	if (b) change(MultiAddition(elements))
	return b
  }

  override fun addAll(elements: Collection<E>): Boolean {
	val b = list.addAll(elements)
	if (b) change(MultiAddition(elements))
	return b
  }

  override fun clear() {
	list.clear()
	change(Clear())
  }


  override fun listIterator() = lItr(list.listIterator())
  override fun listIterator(index: Int) = lItr(list.listIterator(index))

  private fun lItr(itr: MutableListIterator<E>) = object: MListIteratorWithSomeMemory<E>(itr) {
	override fun remove() {
	  super.remove()
	  change(Removal(lastReturned))
	}

	override fun add(element: E) {
	  super.add(element)
	  change(Addition(element))
	}

	override fun set(element: E) {
	  super.set(element)
	  change(Replacement(lastReturned, element))
	}
  }

  override fun remove(element: E): Boolean {
	val b = list.remove(element)
	if (b) change(Removal(element))
	return b
  }

  override fun removeAll(elements: Collection<E>): Boolean {
	val b = list.removeAll(elements)
	if (b) change(MultiAddition(elements))
	return b
  }

  override fun removeAt(index: Int): E {
	val e = list.removeAt(index)
	change(Removal(e))
	return e
  }

  override fun retainAll(elements: Collection<E>): Boolean {
	val toRemove = list.filter { it !in elements }
	val b = list.retainAll(elements)
	if (b) change(MultiRemoval(toRemove))
	return b
  }

  override fun set(index: Int, element: E): E {
	val oldElement = list.set(index, element)
	change(Replacement(removed = oldElement, added = element))
	return oldElement
  }

  override fun subList(fromIndex: Int, toIndex: Int): MutableList<E> {
	return list.subList(fromIndex, toIndex)
  }

}