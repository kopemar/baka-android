package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class Contract(
    @PrimaryKey val id: Int,
    val start_date: String,
    val end_date: String?,
    val work_load: Double?,
    val active: Boolean
)

@JsonClass(generateAdapter = true)
data class ContractResponse(
    val contracts: List<Contract>
)