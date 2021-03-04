package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import androidx.annotation.DrawableRes
import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.DateTimeFormats
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.dayOfWeek
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.formatDate
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class SchedulingUnit(
    val id: Int,
    val start_time: String,
    val end_time: String,
    val is_day: Boolean
) : Serializable {
    val weekDay: String get() = if (is_day) start_time.dayOfWeek() else TODO("Not implemented yet")
    val state: UnitState get() = UnitState.DONE
    val dateF: String get() = if (is_day) start_time.formatDate(DateTimeFormats.FULL_MONTH_DAY) else TODO("Not implemented yet")

    enum class UnitState(@DrawableRes val iconRes: Int) {
        DONE(R.drawable.ic_night_icon)
    }
}

@JsonClass(generateAdapter = true)
class SchedulingUnitsResponse(
    val data: List<SchedulingUnit>
)
