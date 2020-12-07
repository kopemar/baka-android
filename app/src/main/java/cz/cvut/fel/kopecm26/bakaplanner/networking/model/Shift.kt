package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.fullDateShortDayOfWeek

@Entity
@JsonClass(generateAdapter = true)
data class Shift(
    @PrimaryKey val id: Int,
    val start_time: String,
    val end_time: String
) {
    val dateF get() = start_time.fullDateShortDayOfWeek()
}

@JsonClass(generateAdapter = true)
data class ShiftResponse(
    val shifts: List<Shift>
)