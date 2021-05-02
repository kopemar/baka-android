package cz.cvut.fel.kopecm26.bakaplanner.networking.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee

@JsonClass(generateAdapter = true)
data class EmployeeListResponse(
    override val data: List<Employee>,
    @Json(name = "has_next") override val hasNext: Boolean,
    @Json(name = "total_pages") override val totalPages: Int,
    @Json(name = "current_page") override val currentPage: Int,
    override val records: Int = data.size
): PaginatedListResponse<Employee>(data, hasNext, totalPages, currentPage, records)
