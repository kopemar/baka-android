package cz.cvut.fel.kopecm26.bakaplanner.networking.model.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddSpecializationResponse(
    val data: List<EmployeePresenter>
)

