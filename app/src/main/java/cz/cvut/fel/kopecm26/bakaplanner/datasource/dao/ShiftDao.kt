package cz.cvut.fel.kopecm26.bakaplanner.datasource.dao

import androidx.room.Dao
import androidx.room.Query
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

@Dao
interface ShiftDao: BaseDao<Shift> {

    @Query("SELECT * FROM Shift")
    suspend fun getAll(): List<Shift>

}