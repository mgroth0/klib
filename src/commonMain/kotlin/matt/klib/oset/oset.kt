package matt.klib.oset

import matt.klib.olist.Addition
import matt.klib.olist.BasicObservableCollection
import matt.klib.olist.Clear
import matt.klib.olist.MIteratorWithSomeMemory
import matt.klib.olist.MultiAddition
import matt.klib.olist.MultiRemoval
import matt.klib.olist.Removal


fun <E> Collection<E>.toBasicObservableSet(): BasicObservableSet<E> {
  return BasicObservableSet(this)
}

fun <E> Iterable<E>.toBasicObservableSet(): BasicObservableSet<E> {
  return BasicObservableSet(this.toSet())
}

fun <E> Sequence<E>.toBasicObservableSet(): BasicObservableSet<E> {
  return BasicObservableSet(this.toSet())
}

class BasicObservableSet<E>(c: Collection<E> = mutableSetOf()): BasicObservableCollection<E>(), MutableSet<E> {

  private val theSet = c.toMutableSet()

  override val size: Int
	get() = theSet.size

  override fun contains(element: E): Boolean {
	return theSet.contains(element)
  }

  override fun containsAll(elements: Collection<E>): Boolean {
	return theSet.containsAll(elements)
  }

  override fun isEmpty(): Boolean {
	return theSet.isEmpty()
  }

  override fun iterator() = object: MIteratorWithSomeMemory<E>(theSet.iterator()) {
	override fun remove() {
	  super.remove()
	  change(Removal(lastReturned))
	}
  }

  override fun add(element: E): Boolean {
	val b = theSet.add(element)
	//        println("BasicObservableSet.add(${element})")
	if (b) {
	  change(Addition(element))
	}
	return b
  }

  override fun addAll(elements: Collection<E>): Boolean {
	val b = theSet.addAll(elements)
	//        taball("set addAll",elements)
	if (b) change(MultiAddition(elements))
	return b
  }

  override fun clear() {
	//        println("BasicObservableSet.clear")
	theSet.clear()
	change(Clear())
  }

  override fun remove(element: E): Boolean {
	val b = theSet.remove(element)
	//        println("BasicObservableSet.remove(${element})")
	if (b) change(Removal(element))
	return b
  }

  override fun removeAll(elements: Collection<E>): Boolean {
	val b = theSet.removeAll(elements)
	//        taball("set removeAll",elements)
	if (b) change(MultiRemoval(elements))
	return b
  }


  override fun retainAll(elements: Collection<E>): Boolean {
	val toRemove = theSet.filter { it !in elements }
	val b = theSet.retainAll(elements)
	//        taball("set retainAll",elements)
	if (b) change(MultiRemoval(toRemove))
	return b
  }

}