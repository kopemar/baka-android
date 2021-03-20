package cz.cvut.fel.kopecm26.bakaplanner.networking.model.response

import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

@JsonClass(generateAdapter = true)
data class EmployeeShiftsResponse(
    val data: List<Shift>
)
