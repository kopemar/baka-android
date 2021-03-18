package cz.cvut.fel.kopecm26.bakaplanner.networking.model.response

import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod

@JsonClass(generateAdapter = true)
data class SubmitScheduleResponse(
    val data: SchedulingPeriod
)
