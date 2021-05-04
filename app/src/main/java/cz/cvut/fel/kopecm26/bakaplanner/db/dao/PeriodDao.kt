package cz.cvut.fel.kopecm26.bakaplanner.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod

@Dao
interface PeriodDao: BaseDao<SchedulingPeriod> {
    @Query("SELECT * FROM SchedulingPeriod ORDER BY start_date")
    suspend fun getAll(): List<SchedulingPeriod>

    @Query("DELETE FROM SchedulingPeriod")
    suspend fun deleteAll()

    @Transaction
    suspend fun deleteAllAndReplace(entities: Collection<SchedulingPeriod>) {
        deleteAll()
        insert(entities)
    }
}
