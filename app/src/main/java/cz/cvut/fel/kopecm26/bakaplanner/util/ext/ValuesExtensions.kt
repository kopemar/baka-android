package cz.cvut.fel.kopecm26.bakaplanner.util.ext

import com.orhanobut.logger.Logger

/**
 * Allows for null-safe call in [call] block if none of [things] is null.
 */
fun ifNotNull(vararg things: Any?, call: () -> Unit): Boolean? {
    Logger.d("ifNotNull ${things.toList()}")
    if (things.none { it == null }) {
        call.invoke()
        return true
    }
    return null
}
