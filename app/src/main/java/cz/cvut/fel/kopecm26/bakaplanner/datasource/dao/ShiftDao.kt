package cz.cvut.fel.kopecm26.bakaplanner.datasource.dao

import androidx.room.Dao
import androidx.room.Query
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import java.time.LocalDateTime

@Dao
interface ShiftDao: BaseDao<Shift> {

    @Query("SELECT * FROM Shift")
    suspend fun getAll(): List<Shift>

    @Query("SELECT * FROM Shift WHERE end_time > :time")
    suspend fun getUpcoming(time: String = LocalDateTime.now().toString()): List<Shift>

    @Query("SELECT * FROM Shift WHERE start_time < :time AND end_time > :time LIMIT 1")
    suspend fun getHappeningAt(time: String = LocalDateTime.now().toString()): Shift?

}