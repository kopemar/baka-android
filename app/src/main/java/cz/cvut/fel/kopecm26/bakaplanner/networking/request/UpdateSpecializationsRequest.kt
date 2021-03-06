package cz.cvut.fel.kopecm26.bakaplanner.networking.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateSpecializationsRequest(
    val employees: List<Int>
)