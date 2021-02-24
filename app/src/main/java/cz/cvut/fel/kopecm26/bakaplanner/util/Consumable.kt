package cz.cvut.fel.kopecm26.bakaplanner.util

/**
 * Some values (such as success message) can be consumed by multiple fragments.
 */
class Consumable<T>(private val value: T) {
    /**
     * HashMap of all consumers
     */
    private val _consumers = HashMap<String, Boolean>()

    /**
     * Add new consumer (if it doesn't exist yet)
     * @param [key] the consumer key to be added
     * @return <b>true</b> if the consumer has been added (did not exist yet), <b>false</b> otherwise
     */
    fun addConsumer(key: String): Boolean {
        val result = _consumers[key] == null
        _consumers[key] = _consumers[key] ?: false
        return result
    }

    /**
     * @param [key] the consumer key
     * @return Whether a consumer with [key] has already consumed the value.
     */
    private fun hasBeenConsumed(key: String): Boolean = _consumers[key] == true

    fun canBeConsumed(key: String): Boolean = _consumers[key] != null && !hasBeenConsumed(key)

    /**
     * This is the proper way to get a value from consumable.
     * @param [key] that wants to consume the value
     * @return returns the value saved in [value]
     */
    fun consume(key: String): T {
        _consumers[key] = true
        // todo can be consumed only once
        return value
    }

    fun fullyConsumed(): Boolean = _consumers.values.all { it }
}
