package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.db.dao.ShiftDao
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ErrorType
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.weeksAfter
import java.time.LocalDate
import java.time.LocalDateTime

class ShiftRepository(private val service: RemoteDataSource, private val shiftDao: ShiftDao) {

    suspend fun getNextWeekShifts(): List<Shift> = shiftDao.inTimePeriod()

    suspend fun getCurrentShift(): Shift? = shiftDao.getHappeningAt()

    suspend fun getNextShift(): Shift? = shiftDao.getNext()

    suspend fun deleteAll() = shiftDao.deleteAll()

    suspend fun refreshAllShifts(from: LocalDate = LocalDate.now(), to: LocalDate = LocalDate.now().weeksAfter(1)) = service.getShifts(from, to).apply {
        if (this is ResponseModel.SUCCESS) {
            data?.let { shiftDao.insert(it) }
        }
    }

    suspend fun getCachedShifts(from: LocalDateTime, to: LocalDateTime) = if (shiftDao.inTimePeriod(from.toString(), to.toString()).isNullOrEmpty()) {
        refreshAllShifts(from.toLocalDate(), to.toLocalDate())
    } else {
        ResponseModel.SUCCESS(shiftDao.inTimePeriod(from.toString(), to.toString()))
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

    suspend fun getShift(id: Int): ResponseModel<Shift> =
        shiftDao.getById(id)?.let {
            ResponseModel.SUCCESS(it)
        } ?: run { ResponseModel.ERROR(ErrorType(R.string.not_implemented_yet)) }


}
