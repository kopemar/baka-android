package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Auth(val username: String, val password: String)

@JsonClass(generateAdapter = true)
data class User(
    val id: Int,
    val username: String,
    val email: String? = null,
    val agreement: Boolean?,
    val manager: Boolean?,
    val first_name: String? = null,
    val last_name: String? = null,
    val organization_name: String,
    val organization_id: Int,
) {
    val fullName = "$last_name, $first_name"
    val organizationFirstLetter = "${organization_name[0]}"
}

@JsonClass(generateAdapter = true)
data class UserResponseModel(@Json(name = "data") val user: User)

@JsonClass(generateAdapter = true)
data class SignOutModel(val success: Boolean, val errors: List<String>? = null)
