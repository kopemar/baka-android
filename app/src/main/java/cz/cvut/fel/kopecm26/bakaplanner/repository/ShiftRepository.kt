package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.datasource.dao.ShiftDao
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.isBefore

class ShiftRepository(private val service: RemoteDataSource, private val shiftDao: ShiftDao) {

    suspend fun getNextWeekShifts(): List<Shift> = shiftDao.inTimePeriod()

    suspend fun getCurrentShift(): Shift? = shiftDao.getHappeningAt()

    suspend fun getNextShift(): Shift? = shiftDao.getNext()

    suspend fun deleteAll() = shiftDao.deleteAll()

    suspend fun getAllShifts(forceUpdate: Boolean = false, filterUpcoming: Boolean = true): ResponseModel<List<Shift>> {
        return if (forceUpdate || shiftDao.getAll().isNullOrEmpty()) {
            return service.getShifts().apply {
                if (this is ResponseModel.SUCCESS) {
                    data?.let { shiftDao.insert(it) }

                    if (filterUpcoming) data = data?.filter { !it.end_time.isBefore() }
                }
            }
        } else {
            ResponseModel.SUCCESS(shiftDao.getUpcoming())
        }
    }
}