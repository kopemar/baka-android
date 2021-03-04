package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Employee(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val username: String
) : Serializable {
    val fullName: String get() = "$last_name, $first_name"
}
