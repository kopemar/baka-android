package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Shift(
    val start_time: String,
    val end_time: String
)

@JsonClass(generateAdapter = true)
data class ShiftResponse(
    val shifts: List<Shift>
)