package cz.cvut.fel.kopecm26.bakaplanner.db.dao

import androidx.room.Dao
import androidx.room.Query
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Contract

@Dao
interface ContractDao: BaseDao<Contract> {
    @Query("SELECT * FROM Contract ORDER BY start_date")
    suspend fun getAll(): List<Contract>

    @Query("DELETE FROM Contract")
    suspend fun deleteAll()
}