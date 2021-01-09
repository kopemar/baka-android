package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import android.text.format.DateUtils
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.*
import java.io.Serializable
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

@Entity
@JsonClass(generateAdapter = true)
data class Shift(
    @PrimaryKey val id: Int,
    val start_time: String,
    val end_time: String,
    val schedule_id: String?,
    val duration: Int,
    val user_scheduled: Boolean,
): Serializable {
    val dateF get() = start_time.fullDateShortDayOfWeek()

    val startTimeHours get() = start_time.hoursAndMinutes()
    val endTimeHours get() = end_time.hoursAndMinutes()

    val dayOfWeek get() = start_time.dayOfWeek()

    val shiftTime: ShiftTime
        get() = when {
            start_time.isMorning() -> ShiftTime.MORNING
            start_time.isDay() -> ShiftTime.DAY
            start_time.isEvening() -> ShiftTime.EVENING
            else -> ShiftTime.NIGHT
        }

    val relativeTimestamp: String
        get() {
            val time = if (start_time.isBefore()) end_time else start_time

            return DateUtils.getRelativeTimeSpanString(
                ZonedDateTime.parse(time).toEpochSecond() * 1000,
                LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(0)) * 1000,
                DateUtils.MINUTE_IN_MILLIS
            ).toString().toLowerCase(Locale.getDefault())
        }

    val ended: Boolean
        get() = end_time.isBefore()

    val started: Boolean
        get() = start_time.isBefore()

    val canBeRemoved: Boolean
        get() = this.schedule_id != null && start_time.hoursUntilNow() > 96L && user_scheduled
}

@JsonClass(generateAdapter = true)
data class ShiftResponse(
    val shifts: List<Shift>
)

enum class ShiftTime(@DrawableRes val shiftIcon: Int, @ColorRes val colorRes: Int) {
    MORNING(R.drawable.ic_sun_up_icon, R.color.color_primary),
    DAY(R.drawable.ic_sunny_icon, R.color.greeno),
    EVENING(R.drawable.ic_sun_down_icon, R.color.sunny_evenings),
    NIGHT(R.drawable.ic_night_icon, R.color.text)
}