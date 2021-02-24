package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import androidx.annotation.DrawableRes
import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.dayShortMonth
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class SchedulingPeriod(
    val id: Int,
    val start_date: String,
    val end_date: String
) : Serializable {
    val state: PeriodState get() = PeriodState.TO_BE_SUBMITTED

    val dateStartShort get() = start_date.dayShortMonth()
    val dateEndShort get() = end_date.dayShortMonth()
}

enum class PeriodState(@DrawableRes val iconRes: Int) {
    TO_BE_SUBMITTED(R.drawable.ic_night_icon)
}

@JsonClass(generateAdapter = true)
class SchedulingPeriodsResponse(
    val periods: List<SchedulingPeriod>,
    val current_page: Int,
    val has_next: Boolean
)
