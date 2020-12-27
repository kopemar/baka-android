package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.db.dao.ShiftDao
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel

class ScheduleRepository(private val service: RemoteDataSource, private val shiftDao: ShiftDao) {
    suspend fun getSchedulesForShift(shiftId: Int) = service.getSchedulesForShift(shiftId)

    suspend fun addShiftToSchedule(scheduleId: Int, shiftId: Int) = service.addShiftToSchedule(scheduleId, shiftId).apply {
        if (this is ResponseModel.SUCCESS) data?.let { shiftDao.insert(it) }
    }
}