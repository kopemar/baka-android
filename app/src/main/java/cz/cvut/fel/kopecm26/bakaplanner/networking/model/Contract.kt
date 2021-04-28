package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.DateTimeFormats
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.formatDate

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
    val endDate get() = end_date?.formatDate(DateTimeFormats.MONTH_DAY_YEAR)
    val type: ContractTypes? get() = type_id.mapToContractType()
}

enum class ContractTypes(val id: Int, @StringRes val nameRes: Int, @StringRes val nameResTemplate: Int, val type: String) {
    EMPLOYMENT_CONTRACT(1, R.string.employment_contract, R.string.employment_contract_, "EmploymentContract"),
    AGREEMENT_COMPLETE(2, R.string.agreement_complete, R.string.agreement_complete_, "AgreementToCompleteAJob"),
    AGREEMENT_PERFORM(3, R.string.agreement_perform, R.string.agreement_perform_, "AgreementToPerformAJob");

    companion object {
        val all = values().toList().sortedBy { it.id }
    }
}

fun Int.mapToContractType() = ContractTypes.values().firstOrNull { it.id == this }

@JsonClass(generateAdapter = true)
data class ContractResponse(
    val data: List<Contract>
)
