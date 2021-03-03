package cz.cvut.fel.kopecm26.bakaplanner.networking.model.response

import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee

// TODO more generic response (change BE -> data)
@JsonClass(generateAdapter = true)
data class EmployeeListResponse(
    val employees: List<Employee>
)
