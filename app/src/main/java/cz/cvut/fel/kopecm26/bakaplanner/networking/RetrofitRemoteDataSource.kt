package cz.cvut.fel.kopecm26.bakaplanner.networking

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Auth
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodDay
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTimeCalculation
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.User
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateShiftTemplatesRequest
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import cz.cvut.fel.kopecm26.bakaplanner.util.networking.safeApiCall
import okhttp3.Headers
import java.time.ZonedDateTime

class RetrofitRemoteDataSource(private val api: ApiDescription) : RemoteDataSource {
    override suspend fun signIn(auth: Auth): ResponseModel<User> {
        val response = safeApiCall({ api.signIn(auth) }, { it?.user })

        if (response is ResponseModel.SUCCESS<User>) response.headers?.saveUserHeaders()
        return response
    }

    override suspend fun signOut() = safeApiCall({ api.signOut() }, { it })

    override suspend fun getShifts(from: ZonedDateTime, to: ZonedDateTime) =
        safeApiCall({ api.getShifts(from.toString(), to.toString()) }, { it?.shifts })

    override suspend fun getShiftsBefore(to: ZonedDateTime) =
        safeApiCall({ api.getShiftsBefore(to.toString()) }) { it?.shifts }

    override suspend fun getUnassignedShifts(from: ZonedDateTime) =
        safeApiCall({ api.getUnassignedShiftsFrom(from.toString()) }) { it?.templates }

    override suspend fun getContracts() =
        safeApiCall({ api.getContracts() }) { it?.contracts }

    override suspend fun getSchedulesForShift(shiftId: Int) =
        safeApiCall({ api.getSchedulesForShift(shiftId) }) { it?.schedules }

    override suspend fun addShiftToSchedule(shiftId: Int, scheduleId: Int) =
        safeApiCall({ api.addShiftToSchedule(shiftId, scheduleId) }) { it }

    override suspend fun removeShiftFromSchedule(shiftId: Int) =
        safeApiCall({ api.removeShiftFromSchedule(shiftId) }) { it }

    override suspend fun getSchedulingPeriods() =
        safeApiCall({ api.getSchedulingPeriods() }) { it?.periods }

    override suspend fun getSchedulingUnits(periodId: Int) =
        safeApiCall({ api.getSchedulingUnits(periodId) }) { it?.data }

    override suspend fun addShiftTemplate(template: ShiftTemplate) =
        safeApiCall({ api.createTemplate(template) }) { it?.data }

    override suspend fun getShiftTemplates(unitId: Int): ResponseModel<List<ShiftTemplate>> =
        safeApiCall({
            api.getShiftTemplates(unitId)
        }) { it?.templates }

    override suspend fun getTemplateEmployees(templateId: Int): ResponseModel<List<User>> =
        safeApiCall({
            api.getTemplateEmployees(templateId)
        }) { it?.employees }

    override suspend fun getPeriodDays(periodId: Int): ResponseModel<List<PeriodDay>> =
        safeApiCall({
            api.getPeriodDays(periodId)
        }) { it?.days }

    override suspend fun getShiftTimeCalculations(
        periodId: Int,
        startTime: String,
        endTime: String,
        shiftHours: Int,
        breakMinutes: Int,
        perDay: Int
    ): ResponseModel<List<ShiftTimeCalculation>> =
        safeApiCall({
            api.getShiftTimeCalculations(periodId, startTime, endTime, shiftHours, breakMinutes, perDay)
        }) { it?.times }

    override suspend fun createShiftTemplates(
        periodId: Int,
        startTime: String,
        endTime: String,
        shiftHours: Int,
        breakMinutes: Int,
        perDay: Int,
        excluded: Map<Int, ArrayList<Int>>,
        workingDays: List<Int>
    ): ResponseModel<List<ShiftTemplate>> = safeApiCall({
        api.createShiftTemplates(periodId, CreateShiftTemplatesRequest(startTime, endTime, shiftHours, breakMinutes, perDay, excluded, workingDays))
    }) { it?.templates }

    private fun Headers.saveUserHeaders() =
        Constants.UserHeaders.values().forEach { getAndSave(it.key) }

    private fun Headers.getAndSave(key: String) = get(key)?.let { PrefsUtils.putString(key, it) }
}
