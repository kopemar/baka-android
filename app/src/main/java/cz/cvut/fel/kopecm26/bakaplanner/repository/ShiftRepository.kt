package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.datasource.dao.ShiftDao
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

class ShiftRepository(private val service: RemoteDataSource, private val shiftDao: ShiftDao) {

    suspend fun getNextWeekShifts(): List<Shift> = shiftDao.inTimePeriod()

    suspend fun getCurrentShift(): Shift? = shiftDao.getHappeningAt()

    suspend fun getNextShift(): Shift? = shiftDao.getNext()

    suspend fun deleteAll() = shiftDao.deleteAll()

    suspend fun refreshAllShifts() = service.getShifts().apply {
        if (this is ResponseModel.SUCCESS) {
            data?.let { shiftDao.insert(it) }
        }
    }

    suspend fun getCachedShifts() = if (shiftDao.getAll().isNullOrEmpty()) {
        refreshAllShifts()
    } else {
        ResponseModel.SUCCESS(shiftDao.getAll())
    }

    suspend fun getUpcomingShifts(): ResponseModel<List<Shift>> {
        if (shiftDao.getAll().isNullOrEmpty()) {
            val result = refreshAllShifts()
            if (result !is ResponseModel.SUCCESS) {
                return result
            }
        }
        return ResponseModel.SUCCESS(shiftDao.getUpcoming())
    }
}
