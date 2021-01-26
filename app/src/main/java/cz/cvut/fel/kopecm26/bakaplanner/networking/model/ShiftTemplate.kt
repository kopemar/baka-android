package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import androidx.annotation.StringRes
import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.R
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
    val priorityValue: Priority? = Priority.getPriorityByValue(priority)

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

enum class Priority(@StringRes val titleRes: Int, val integerValue: Int) {
    HIGHEST(R.string.highest, 5),
    HIGH(R.string.high, 4),
    MEDIUM(R.string.medium, 3),
    LOW(R.string.low, 2),
    LOWEST(R.string.lowest, 1);

    companion object {
        fun getPriorityByValue(value: Int) = values().find { it.integerValue == value }
    }
}