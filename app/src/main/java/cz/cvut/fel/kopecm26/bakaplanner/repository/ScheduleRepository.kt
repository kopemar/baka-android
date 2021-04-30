package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.db.dao.ShiftDao
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.AddShiftToSchedules

class ScheduleRepository(private val service: RemoteDataSource, private val shiftDao: ShiftDao) {
    suspend fun getSchedulesForShift(shiftId: Int) = service.getSchedulesForShift(shiftId)

    suspend fun addShiftToSchedules(shiftId: Int, schedules: List<Int>) = service.addShiftToSchedules(shiftId, AddShiftToSchedules(schedules))
}
