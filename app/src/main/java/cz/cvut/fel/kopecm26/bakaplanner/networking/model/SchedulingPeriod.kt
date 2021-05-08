package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodState.Companion.getState
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.DateTimeFormats
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.formatDate
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.getLocalDate
import java.io.Serializable
import java.time.LocalDate

@JsonClass(generateAdapter = true)
@Entity
data class SchedulingPeriod(
    @PrimaryKey val id: Int,
    val start_date: String,
    val end_date: String,
    val submitted: Boolean,
    val planned: Boolean = false,
    val days_left: Int? = null,
    val units_exist: Boolean? = null
) : Serializable {
    val state: PeriodState get() = getState()

    val dateStartShort get() = start_date.formatDate(DateTimeFormats.FULL_MONTH_DAY_SHORT)
    val dateEndShort get() = end_date.formatDate(DateTimeFormats.FULL_MONTH_DAY_SHORT)
}

enum class PeriodState(
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int = R.drawable.ic_calendar
) {
    PAST(R.string.past),
    CURRENT(R.string.current, R.drawable.ic_calendar),
    TO_BE_SUBMITTED(R.string.to_be_submitted, R.drawable.ic_fa_solid_calendar_plus),
    SUBMITTED(R.string.submitted, R.drawable.ic_fa_solid_calendar_check),
    TO_BE_PLANNED(R.string.to_be_planned, R.drawable.ic_mdi_alert);

    companion object {
        fun SchedulingPeriod.getState(): PeriodState = when {
            start_date.getLocalDate().isBefore(LocalDate.now()) && end_date.getLocalDate().isBefore(LocalDate.now()) -> PAST
            start_date.getLocalDate().isBefore(LocalDate.now()) || start_date.getLocalDate().isEqual(LocalDate.now()) -> CURRENT
            submitted -> SUBMITTED
            planned -> TO_BE_SUBMITTED
            else -> TO_BE_PLANNED
        }
    }
}

@JsonClass(generateAdapter = true)
class SchedulingPeriodsResponse(
    val periods: List<SchedulingPeriod>,
    val current_page: Int,
    val has_next: Boolean
)
