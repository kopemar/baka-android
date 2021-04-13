package cz.cvut.fel.kopecm26.bakaplanner.networking.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateOrganizationRequest(
    val name: String,
    val username: String,
    val email: String,
    val password: String,
    val first_name: String,
    val last_name: String,
)
