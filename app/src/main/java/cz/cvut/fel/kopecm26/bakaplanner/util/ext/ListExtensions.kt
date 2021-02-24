package cz.cvut.fel.kopecm26.bakaplanner.util.ext

fun <T : DataClass<T>> List<T>.deepCopy(): List<T> = mapNotNull { it.copyData() }

interface DataClass<W> {
    fun copyData(): W
}
