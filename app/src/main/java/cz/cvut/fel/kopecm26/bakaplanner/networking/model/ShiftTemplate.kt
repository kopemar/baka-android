package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.DateTimeFormats
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.formatDate
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.formatTime
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class ShiftTemplate(
    val id: Int?,
    val start_time: String,
    val end_time: String,
    val break_minutes: Int,
    val priority: Int,
    val duration: Float?,
    val shifts_count: Int? = null,
    val specialization: String? = null,
) : Serializable {
    val startTimeHours get() = start_time.formatTime(DateTimeFormats.HOURS_MINUTES)
    val endTimeHours get() = end_time.formatTime(DateTimeFormats.HOURS_MINUTES)
    val dateF get() = start_time.formatDate(DateTimeFormats.FULL_MONTH_DAY)
    val priorityValue: Priority? = Priority.getPriorityByValue(priority)

    val shiftTime: ShiftTime get() = ShiftTime.getFromTime(start_time)
}

@JsonClass(generateAdapter = true)
data class ShiftTemplateResponse(
    val data: ShiftTemplate
)

@JsonClass(generateAdapter = true)
data class ShiftTemplatesResponse(
    val data: List<ShiftTemplate>
)

enum class Priority(@StringRes val titleRes: Int, val integerValue: Int, @ColorRes val color: Int, @DrawableRes val iconRes: Int) {
    HIGHEST(R.string.highest, 5, R.color.red, R.drawable.ic_mdi_chevron_triple_up),
    HIGH(R.string.high, 4, R.color.orange_10000, R.drawable.ic_mdi_chevron_up),
    MEDIUM(R.string.medium, 3, R.color.yellow, R.drawable.ic_mdi_tilde),
    LOW(R.string.low, 2, R.color.greeno, R.drawable.ic_mdi_chevron_down),
    LOWEST(R.string.lowest, 1, R.color.light_blue, R.drawable.ic_mdi_chevron_triple_down),
    NONE(R.string.remove, 0, R.color.gray_blue, R.drawable.ic_fa_solid_trash);

    companion object {
        fun getPriorityByValue(value: Int) = values().find { it.integerValue == value }
    }
}
