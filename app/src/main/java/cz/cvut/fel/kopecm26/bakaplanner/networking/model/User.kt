package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Auth(val username: String, val password: String)

@JsonClass(generateAdapter = true)
data class User(
    val username: String,
    val email: String? = null,
    val first_name: String? = null,
    val last_name: String? = null
)

@JsonClass(generateAdapter = true)
data class UserResponseModel(@Json(name = "data") val user: User)

@JsonClass(generateAdapter = true)
data class SuccessModel(val success: Boolean)