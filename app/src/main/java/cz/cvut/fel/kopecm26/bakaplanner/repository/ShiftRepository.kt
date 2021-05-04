package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.db.dao.PeriodDao
import cz.cvut.fel.kopecm26.bakaplanner.db.dao.ShiftDao
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ErrorType
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import java.time.ZonedDateTime

class ShiftRepository(
    private val service: RemoteDataSource,
    private val shiftDao: ShiftDao,
    private val periodDao: PeriodDao,
) {

    suspend fun getNextWeekShifts(): List<Shift> = shiftDao.inTimePeriod()

    suspend fun getCurrentShift(): Shift? = shiftDao.getHappeningAt()

    suspend fun getNextShift(): Shift? = shiftDao.getNext()

    suspend fun deleteAll() = shiftDao.deleteAll()

    suspend fun refreshAllShifts(): ResponseModel<List<Shift>> = service.getShifts().run {
        if (this is ResponseModel.SUCCESS) {
            data?.let { shiftDao.deleteAllAndReplace(it) }
            ResponseModel.SUCCESS(shiftDao.getUpcoming())
        } else this
    }

    suspend fun refreshShiftsBefore(to: ZonedDateTime = ZonedDateTime.now()): ResponseModel<List<Shift>> {
        service.getShifts().apply {
            if (PrefsUtils.getUser()?.manager == true) return this
            if (this is ResponseModel.SUCCESS) {
                data?.let { shiftDao.deleteAllAndReplace(it) }
            }
        }
        return ResponseModel.SUCCESS(shiftDao.getBefore(to.toString()))
    }

    suspend fun getShiftsBefore(to: ZonedDateTime): ResponseModel<List<Shift>> {
        val response = refreshShiftsBefore(to)
        return if (response is ResponseModel.ERROR) {
            ResponseModel.SUCCESS(shiftDao.getBefore(to.toString()))
        } else {
            response
        }
    }

    suspend fun getAllPeriods(from: ZonedDateTime = ZonedDateTime.now()): ResponseModel<List<SchedulingPeriod>> {
        val response = service.getSchedulingPeriods(from)
        return if (response is ResponseModel.ERROR) {
            ResponseModel.SUCCESS(periodDao.getAll())
        } else {
            response.also {
                if (it is ResponseModel.SUCCESS) it.data?.let { periods ->
                    periodDao.deleteAllAndReplace(periods)
                }
            }
        }
    }

    suspend fun getAllShifts(): ResponseModel<List<Shift>> {
        refreshAllShifts().let {
            return if (it is ResponseModel.SUCCESS) {
                it.also {
                    it.data?.let { shifts -> shiftDao.deleteAllAndReplace(shifts) }
                }
            } else {
                val cached = shiftDao.getAll()
                if (cached.isNullOrEmpty()) it else ResponseModel.SUCCESS(cached)
            }
        }
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
            if (this is ResponseModel.SUCCESS) { shiftDao.deleteById(shiftId) }
        }
}
