package cz.cvut.fel.kopecm26.bakaplanner.util

class Selection<T>(val item: T, var selected: Boolean = false) {

    override fun equals(other: Any?): Boolean {
        if (other !is Selection<*>) return false
        return other.item.hashCode() == item.hashCode()
    }

    override fun hashCode(): Int {
        return item?.hashCode() ?: 0
    }
}

class SelectionList<W : Selection<*>> : ArrayList<W>() {

    fun select(item: W, onSelect: ((index: Int) -> Unit)? = null) {
        find { it == item }?.selected = true
        onSelect?.invoke(indexOfFirst { it.item.hashCode() == item.hashCode() })
    }

    fun unselect(item: W, onSelect: ((index: Int) -> Unit)? = null) {
        find { it == item }?.selected = false
        onSelect?.invoke(indexOfFirst { it == item })
    }
}


class SingleSelectionList<W : Selection<*>> : ArrayList<W>() {
    private fun getSelectionIndex(): Int = indexOfFirst { it.selected }

    val selected: W? get() = find { it.selected }

    fun select(item: W, onSelect: ((newIndex: Int, originalIndex: Int) -> Unit)? = null) {
        val original = getSelectionIndex()
        find { it.selected }?.selected = false
        find { it == item }?.selected = true
        onSelect?.invoke(getSelectionIndex(), original)
    }
}

fun <W> selectionListOf(list: List<W>, selected: Boolean = false): SelectionList<Selection<W>> = SelectionList<Selection<W>>().apply {
    addAll(list.map { Selection(it, selected) })
}
