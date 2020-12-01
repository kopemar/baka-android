package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Auth (val username: String, val password: String)

@JsonClass(generateAdapter = true)
data class User(val username: String)

@JsonClass(generateAdapter = true)
data class UserResponseModel(@Json(name = "data") val user: User)