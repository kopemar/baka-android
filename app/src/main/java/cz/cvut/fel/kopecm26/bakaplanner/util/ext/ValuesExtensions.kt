package cz.cvut.fel.kopecm26.bakaplanner.util.ext

fun ifNotNull(vararg things: Any?, call: () -> Unit) {
    if (things.none { it == null }) call.invoke()
}