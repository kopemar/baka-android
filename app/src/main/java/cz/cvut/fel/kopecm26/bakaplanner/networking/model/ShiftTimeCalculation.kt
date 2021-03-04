package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.DataClass
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.DateTimeFormats
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.formatTime

@JsonClass(generateAdapter = true)
data class ShiftTimeCalculation(
    val start_time: String,
    val end_time: String,
    val id: Int
) : DataClass<ShiftTimeCalculation> {
    val startTime = start_time.formatTime(DateTimeFormats.HOURS_MINUTES)
    val endTime = end_time.formatTime(DateTimeFormats.HOURS_MINUTES)
    var checked = true
    val shiftTime get() = ShiftTime.getFromTime(start_time)

    override fun copyData(): ShiftTimeCalculation = copy(
        start_time = start_time,
        end_time = end_time,
        id = id
    )
}

@JsonClass(generateAdapter = true)
data class ShiftTimeCalculationResponse(
    val times: List<ShiftTimeCalculation>
)
