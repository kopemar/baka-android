package cz.cvut.fel.kopecm26.bakaplanner.db.dao

import androidx.room.Dao
import androidx.room.Query
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters

@Dao
interface ShiftDao: BaseDao<Shift> {

    @Query("SELECT * FROM Shift WHERE id = :id")
    suspend fun getById(id: Int): Shift?

    @Query("SELECT * FROM Shift ORDER BY start_time")
    suspend fun getAll(): List<Shift>

    @Query("SELECT * FROM Shift WHERE end_time <= :time ORDER BY start_time DESC")
    suspend fun getBefore(time: String = LocalDateTime.now().toString()): List<Shift>

    @Query("SELECT * FROM Shift WHERE end_time > :time ORDER BY start_time")
    suspend fun getUpcoming(time: String = LocalDateTime.now().toString()): List<Shift>

    @Query("SELECT * FROM Shift WHERE start_time > :start AND start_time < :end ORDER BY start_time")
    suspend fun inTimePeriod(start: String = LocalDateTime.now().toString(), end: String = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).toString()): List<Shift>

    @Query("SELECT * FROM Shift WHERE start_time < :time AND end_time > :time LIMIT 1")
    suspend fun getHappeningAt(time: String = LocalDateTime.now().toString()): Shift?

    @Query("SELECT * FROM Shift WHERE start_time > :time ORDER BY start_time LIMIT 1")
    suspend fun getNext(time: String = LocalDateTime.now().toString()): Shift?

    @Query("DELETE FROM Shift")
    suspend fun deleteAll()
}