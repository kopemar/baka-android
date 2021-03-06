package cz.cvut.fel.kopecm26.bakaplanner.networking.model.response

import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee

@JsonClass(generateAdapter = true)
data class EmployeeResponse(
    val employees: List<Employee>
)

