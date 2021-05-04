package cz.cvut.fel.kopecm26.bakaplanner.networking.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SchedulingParams(
    val priorities: SchedulingPriorities,
    val iterations: Int = 1,
)

@JsonClass(generateAdapter = true)
data class SchedulingPriorities(
    val no_empty_shifts: Int = 10,
    val free_days: Int = 200,
    val demand_fulfill: Int = 50,
    val specialized_preferred: Int = 30
)
