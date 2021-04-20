package cz.cvut.fel.kopecm26.bakaplanner.networking.model.response

import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Contract

@JsonClass(generateAdapter = true)
data class CreateContractResponse(
    val data: Contract,
    val success: Boolean,
)
