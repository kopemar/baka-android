package cz.cvut.fel.kopecm26.bakaplanner.networking.model.response

import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Specialization

@JsonClass(generateAdapter = true)
data class SpecializationsResponse(
    val data: List<Specialization>
)
