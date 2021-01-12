package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.db.dao.ShiftDao
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ErrorType
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.weeksAfter
import java.time.ZonedDateTime

class ShiftRepository(private val service: RemoteDataSource, private val shiftDao: ShiftDao) {

    suspend fun getNextWeekShifts(): List<Shift> = shiftDao.inTimePeriod()

    suspend fun getCurrentShift(): Shift? = shiftDao.getHappeningAt()

    suspend fun getNextShift(): Shift? = shiftDao.getNext()

    suspend fun deleteAll() = shiftDao.deleteAll()

    suspend fun refreshAllShifts(
        from: ZonedDateTime = ZonedDateTime.now(),
        to: ZonedDateTime = ZonedDateTime.now().weeksAfter(2)
    ): ResponseModel<List<Shift>> = service.getShifts(from, to).run {
        if (this is ResponseModel.SUCCESS) {
            data?.let { shiftDao.insert(it) }
            ResponseModel.SUCCESS(shiftDao.getUpcoming(from.toString()))
        } else this

    }

    suspend fun refreshShiftsBefore(to: ZonedDateTime = ZonedDateTime.now()) =
        service.getShiftsBefore(to).apply {
            if (this is ResponseModel.SUCCESS) {
                data?.let { shiftDao.insert(it) }
            }
        }

    suspend fun getCachedShifts(from: ZonedDateTime, to: ZonedDateTime) =
        if (shiftDao.inTimePeriod(from.toString(), to.toString()).isNullOrEmpty()) {
            refreshAllShifts(from, to)
        } else {
            ResponseModel.SUCCESS(shiftDao.inTimePeriod(from.toString(), to.toString()))
        }

    suspend fun getShiftsBefore(to: ZonedDateTime): ResponseModel<List<Shift>> =
        if (shiftDao.getBefore(to.toString()).isNullOrEmpty()) {
            service.getShiftsBefore(to)
        } else {
            ResponseModel.SUCCESS(shiftDao.getBefore(to.toString()))
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

    suspend fun getUnassigned(): ResponseModel<List<ShiftTemplate>> = service.getUnassignedShifts()

    suspend fun getShift(id: Int): ResponseModel<Shift> =
        shiftDao.getById(id)?.let {
            ResponseModel.SUCCESS(it)
        } ?: run { ResponseModel.ERROR(ErrorType(R.string.not_implemented_yet)) }

    suspend fun addShiftToSchedule(shiftId: Int, scheduleId: Int) =
        service.addShiftToSchedule(shiftId, scheduleId).apply {
            if (this is ResponseModel.SUCCESS) data?.let { shiftDao.insert(it) }
        }

    suspend fun removeShiftFromSchedule(shiftId: Int) =
        service.removeShiftFromSchedule(shiftId).apply {
            if (this is ResponseModel.SUCCESS) data?.id?.let { shiftDao.deleteById(it) }
        }
}
