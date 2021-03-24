package cz.cvut.fel.kopecm26.bakaplanner.networking.model.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmployeePresenter(
    val id: Int,
    val first_name: String,
    val last_name: String,
) {
    val fullName: String get() = "$last_name, $first_name"
    val firstLetters: String get() = "${first_name[0]}${last_name[0]}"
}