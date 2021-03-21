package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Specialization(
    val id: Int,
    val name: String,
)
