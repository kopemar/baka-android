package cz.cvut.fel.kopecm26.bakaplanner.util.ext

import java.time.LocalTime.parse
import java.time.format.DateTimeFormatter

fun String.fullDateShortDayOfWeek(): String? = parse(this).format(DateTimeFormatter.ofPattern(DateTimeFormats.FULL_DATE_SHORT_DAY))

object DateTimeFormats {
    const val FULL_DATE_SHORT_DAY = "E MM d, yy"
}