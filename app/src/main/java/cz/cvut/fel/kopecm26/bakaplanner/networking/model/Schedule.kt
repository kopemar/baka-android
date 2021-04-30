package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Schedule(
    val id: Int,
    val contract_type: Int,
    val first_name: String?,
    val last_name: String?
) {
    val type get() = contract_type.mapToContractType()
}

@JsonClass(generateAdapter = true)
data class ScheduleResponse(
    val data: List<Schedule>
)
