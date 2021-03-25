package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.DateTimeFormats
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.formatDate

/**
 * Serves as simple Presenter class for any combination of date and ID
 */
@JsonClass(generateAdapter = true)
data class PeriodDay(
    val date: String,
    val id: Int
) {

    val dayOfWeek = date.formatDate(DateTimeFormats.WEEK_DAY)
    val dateF = date.formatDate(DateTimeFormats.FULL_MONTH_DAY)
    val dayOfMonth = date.formatDate(DateTimeFormats.DAY_OF_MONTH)

    @Deprecated("use [Selection] instead")
    var checked: Boolean = true
}

@JsonClass(generateAdapter = true)
class PeriodDaysResponse(
    val days: List<PeriodDay>
)
