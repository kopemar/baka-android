package cz.cvut.fel.kopecm26.bakaplanner.util

class TwoWayListIterator<T>(val items: List<T>) : ListIterator<T> {
    private var current = -1

    override fun hasNext() = items.size > nextIndex()

    override fun hasPrevious() = previousIndex() >= 0

    override fun next(): T {
        current = nextIndex()
        return items[current]
    }

    override fun nextIndex() = current + 1

    override fun previous(): T {
        current = previousIndex()
        return items[current]
    }

    override fun previousIndex(): Int = current - 1
}
