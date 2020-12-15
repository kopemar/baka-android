package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import android.text.format.DateUtils
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.dayOfWeek
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.fullDateShortDayOfWeek
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hoursAndMinutes
import java.time.ZonedDateTime
import java.util.*

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

    val dayOfWeek get() = start_time.dayOfWeek()

    val relativeTimestamp
        get() = DateUtils.getRelativeTimeSpanString(
            ZonedDateTime.parse(start_time).toEpochSecond() * 1000,
            ZonedDateTime.now().toEpochSecond() * 1000,
            DateUtils.MINUTE_IN_MILLIS
        ).toString().toLowerCase(Locale.ROOT)
}

@JsonClass(generateAdapter = true)
data class ShiftResponse(
    val shifts: List<Shift>
)

enum class ShiftTime(@DrawableRes val shiftIcon: Int, @ColorRes val colorRes: Int) {
    MORNING(R.drawable.ic_weather_sunset_up, R.color.light_blue),
    DAY(R.drawable.ic_weather_sunny, R.color.greeno),
    EVENING(R.drawable.ic_weather_sunset_down, R.color.sunny_evenings),
    NIGHT(R.drawable.ic_weather_night, R.color.text)
}