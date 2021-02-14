package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShiftTimeCalculation(
    val start_time: String,
    val end_time: String,
    val id: Int
)

@JsonClass(generateAdapter = true)
data class ShiftTimeCalculationResponse(
    val times: List<ShiftTimeCalculation>
)