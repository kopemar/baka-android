package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.fullDateShortDayOfWeek
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hoursAndMinutes
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.isBefore

@Entity
@JsonClass(generateAdapter = true)
data class Shift(
    @PrimaryKey val id: Int,
    val start_time: String,
    val end_time: String
) {
    val dateF get() = start_time.fullDateShortDayOfWeek()

    val startTimeHours get() = start_time.hoursAndMinutes()
    val endTimeHours get() = end_time.hoursAndMinutes()

    val state: ShiftState get() = when {
        end_time.isBefore() -> ShiftState.PAST
        start_time.isBefore() -> ShiftState.NOW
        else -> ShiftState.FUTURE
    }
}

@JsonClass(generateAdapter = true)
data class ShiftResponse(
    val shifts: List<Shift>
)

enum class ShiftState(@DrawableRes val shiftIcon: Int) {
    PLANNING(R.drawable.ic_shift_planning),
    FUTURE(R.drawable.ic_shift_future),
    NOW(R.drawable.ic_shift_now),
    PAST(R.drawable.ic_shift_past)
}