package cz.cvut.fel.kopecm26.bakaplanner.networking.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class CreateShiftTemplatesRequest(
    val start_time: String,
    val end_time: String,
    val shift_hours: Int,
    val break_minutes: Int,
    val per_day: Int,
    val excluded: Map<Int, List<Int>>,
    val working_days: List<Int>,
)
