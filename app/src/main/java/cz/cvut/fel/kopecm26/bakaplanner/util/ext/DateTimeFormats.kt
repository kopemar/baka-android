package cz.cvut.fel.kopecm26.bakaplanner.util.ext

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String.fullDateShortDayOfWeek(): String? = LocalDate.parse(substring(0, 10)).format(DateTimeFormatter.ofPattern(DateTimeFormats.FULL_DATE_SHORT_DAY))

object DateTimeFormats {
    const val FULL_DATE_SHORT_DAY = "E, MMMM d, yyyy"
}