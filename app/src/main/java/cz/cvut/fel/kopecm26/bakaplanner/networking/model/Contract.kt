package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.R

@Entity
@JsonClass(generateAdapter = true)
data class Contract(
    @PrimaryKey val id: Int,
    val start_date: String,
    val end_date: String?,
    val work_load: Double?,
    val active: Boolean,
    @Json(name = "type") val type_id: Int
) {
    val type: ContractTypes? get() = type_id.mapToContractType()
}

enum class ContractTypes(val id: Int, @StringRes val nameRes: Int) {
    EMPLOYMENT_CONTRACT(1, R.string.employment_contract),
    AGREEMENT_COMPLETE(2, R.string.agreement_complete),
    AGREEMENT_PERFORM(3, R.string.agreement_perform),
}

fun Int.mapToContractType() = ContractTypes.values().firstOrNull { it.id == this }

@JsonClass(generateAdapter = true)
data class ContractResponse(
    val contracts: List<Contract>
)
