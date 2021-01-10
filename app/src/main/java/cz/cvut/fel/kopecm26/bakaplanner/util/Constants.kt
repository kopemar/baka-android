package cz.cvut.fel.kopecm26.bakaplanner.util

object Constants {
    object Prefs {
        const val BASE_URL = "BASE_URL"
        const val USER = "USER"
    }

    object Time {
        const val MINUTES_IN_HOUR = 60
    }

    enum class UserHeaders(val key: String) {
        ACCESS_TOKEN(Headers.ACCESS_TOKEN),
        TOKEN_TYPE(Headers.TOKEN_TYPE),
        CLIENT(Headers.CLIENT),
        EXPIRY(Headers.EXPIRY),
        UID(Headers.UID)
    }

    object Headers {
        const val ACCESS_TOKEN = "access-token"
        const val TOKEN_TYPE = "token-type"
        const val CLIENT = "client"
        const val EXPIRY = "expiry"
        const val UID = "uid"
    }
}