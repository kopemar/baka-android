package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShiftTemplate(
    val id: Int?,
    val start_time: String,
    val end_time: String,
    val break_minutes: Int,
    val priority: Int
)

@JsonClass(generateAdapter = true)
data class ShiftTemplateResponse(
    val data: ShiftTemplate
)