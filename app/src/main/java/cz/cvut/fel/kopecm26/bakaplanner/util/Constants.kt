package cz.cvut.fel.kopecm26.bakaplanner.util

object Constants {
    object Prefs {
        const val BASE_URL = "BASE_URL"
        const val USER = "USER"
    }

    enum class UserHeaders(val key: String) {
        ACCESS_TOKEN("access-token"),
        TOKEN_TYPE("token-type"),
        CLIENT("client"),
        EXPIRY("expiry"),
        UID("uid")
    }
}