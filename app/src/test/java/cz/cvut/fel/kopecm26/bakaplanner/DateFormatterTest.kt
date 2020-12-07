package cz.cvut.fel.kopecm26.bakaplanner

import cz.cvut.fel.kopecm26.bakaplanner.util.ext.fullDateShortDayOfWeek
import org.junit.Assert
import org.junit.Test

class DateFormatterTest {
    @Test
    fun fullDateShortDayOfWeek() {
        Assert.assertEquals("Fri, November 13, 2020", "2020-11-13T11:00:00.000Z".fullDateShortDayOfWeek())
    }
}