package cz.cvut.fel.kopecm26.bakaplanner.util.ext

/**
 * Allows for null-safe call in [call] block if none of [things] is null.
 */
fun ifNotNull(vararg things: Any?, call: () -> Unit) {
    if (things.none { it == null }) call.invoke()
}
