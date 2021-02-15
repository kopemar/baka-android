package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hoursAndMinutes

@JsonClass(generateAdapter = true)
data class ShiftTimeCalculation(
    val start_time: String,
    val end_time: String,
    val id: Int
) {
    val startTime = start_time.hoursAndMinutes()
    val endTime = end_time.hoursAndMinutes()
}

@JsonClass(generateAdapter = true)
data class ShiftTimeCalculationResponse(
    val times: List<ShiftTimeCalculation>
)