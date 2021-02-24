package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.db.dao.ShiftDao

class ScheduleRepository(private val service: RemoteDataSource, private val shiftDao: ShiftDao) {
    suspend fun getSchedulesForShift(shiftId: Int) = service.getSchedulesForShift(shiftId)
}
