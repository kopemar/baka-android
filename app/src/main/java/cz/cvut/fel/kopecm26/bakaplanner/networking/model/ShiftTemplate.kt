package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.*
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class ShiftTemplate(
    val id: Int?,
    val start_time: String,
    val end_time: String,
    val break_minutes: Int,
    val priority: Int,
    val duration: Float?
): Serializable {
    val startTimeHours get() = start_time.hoursAndMinutes()
    val endTimeHours get() = end_time.hoursAndMinutes()
    val dateF get() = start_time.fullDate()

    val shiftTime: ShiftTime
        get() = when {
            start_time.isMorning() -> ShiftTime.MORNING
            start_time.isDay() -> ShiftTime.DAY
            start_time.isEvening() -> ShiftTime.EVENING
            else -> ShiftTime.NIGHT
        }
}

@JsonClass(generateAdapter = true)
data class ShiftTemplateResponse(
    val data: ShiftTemplate
)

@JsonClass(generateAdapter = true)
data class ShiftTemplatesResponse(
    val data: List<ShiftTemplate>
)