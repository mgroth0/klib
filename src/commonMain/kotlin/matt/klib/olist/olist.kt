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
}

sealed interface ListChange<E>
class Addition<E>(val added: E): ListChange<E>
class MultiAddition<E>(val added: Collection<E>): ListChange<E>
class Removal<E>(val removed: E): ListChange<E>
class MultiRemoval<E>(val removed: Collection<E>): ListChange<E>
class Replacement<E>(val removed: E, val added: E): ListChange<E>
class Clear<E>: ListChange<E>

open class MListIteratorWithSomeMemory<E>(
  private val itr: MutableListIterator<E>,
): MutableListIterator<E> by itr {
  private var hadFirstReturn = false

  private var _lastReturned: E? = null

  val lastReturned: E
	get() = if (!hadFirstReturn) err("hasn't returned yet") else _lastReturned as E

  override fun next(): E {
	return itr.next().also {
	  hadFirstReturn = true
	  _lastReturned = it
	}
  }

  override fun previous(): E {
	return itr.previous().also {
	  hadFirstReturn = true
	  _lastReturned = it
	}
  }

}

class BasicObservableList<E>(c: Collection<E> = mutableListOf()): MutableList<E>, MObservable<ListChange<E>> {

  private val list = c.toMutableList()


  private val listeners = mutableListOf<(ListChange<E>)->Unit>()
  override fun onChange(listener: (ListChange<E>)->Unit): (ListChange<E>)->Unit {
	listeners.add(listener)
	return listener
  }

  private fun change(change: ListChange<E>) {
	listeners.forEach { it(change) }
  }


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