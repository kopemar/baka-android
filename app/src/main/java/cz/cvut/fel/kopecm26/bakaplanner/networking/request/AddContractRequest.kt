package cz.cvut.fel.kopecm26.bakaplanner.networking.request

import androidx.annotation.FloatRange

data class AddContractRequest(
    val employee_id: Int,
    val start_date: String,
    val end_date: String?,
    @FloatRange(from = 0.0, to = 1.0) val work_load: Float?,
    val type: String
)
