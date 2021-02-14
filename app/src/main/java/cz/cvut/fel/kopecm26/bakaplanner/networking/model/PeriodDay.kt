package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.dayFullMonth
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.dayOfWeek

@JsonClass(generateAdapter = true)
data class PeriodDay(
    val date: String,
    val id: Int
) {

    val dayOfWeek = date.dayOfWeek()
    val dateF = date.dayFullMonth()
    var checked: Boolean = false
}

@JsonClass(generateAdapter = true)
class PeriodDaysResponse(
    val days: List<PeriodDay>
)