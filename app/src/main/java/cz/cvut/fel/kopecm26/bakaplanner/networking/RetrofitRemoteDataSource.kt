package cz.cvut.fel.kopecm26.bakaplanner.networking

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Auth
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.User
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.AddShiftToSchedules
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateContractRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateEmployeeRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateOrganizationRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateShiftTemplatesRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateSpecializationRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.SchedulingParams
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.UpdateSpecializationsRequest
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

    override suspend fun signUp(request: CreateOrganizationRequest) =
        safeApiCall({
            api.postOrganization(request)
        }) { it }

    override suspend fun createEmployee(request: CreateEmployeeRequest) =
        safeApiCall({
            api.postEmployee(request)
        }) { it }

    override suspend fun postFirebaseToken(token: String) = safeApiCall({
        api.postFirebaseToken(token)
    }) { true }

    override suspend fun getShifts() =
        safeApiCall({ api.getShifts() }, { it?.shifts })

    override suspend fun getShiftsBefore(to: ZonedDateTime) =
        safeApiCall({ api.getShiftsBefore(to.toString()) }) { it?.shifts }

    override suspend fun getUnassignedShifts(from: ZonedDateTime) =
        safeApiCall({ api.getUnassignedShiftsFrom(from.toString()) }) { it?.data }

    override suspend fun getContracts() =
        safeApiCall({ api.getContracts() }) { it?.data }

    override suspend fun getEmployeeContracts(employeeId: Int) =
        safeApiCall({ api.getEmployeeContracts(employeeId) }) { it?.data }

    override suspend fun createContract(
        employeeId: Int,
        request: CreateContractRequest
    ) = safeApiCall({
        api.createContract(request)
    }) { it }

    override suspend fun getSchedulesForShift(shiftId: Int) =
        safeApiCall({ api.getSchedulesForShift(shiftId) }) { it?.data }

    override suspend fun addShiftToSchedule(shiftId: Int, scheduleId: Int) =
        safeApiCall({ api.addShiftToSchedule(shiftId, scheduleId) }) { it }

    override suspend fun addShiftToSchedules(shiftId: Int, request: AddShiftToSchedules) =
        safeApiCall({ api.addShiftToSchedules(shiftId, request) }) { it?.data }

    override suspend fun removeShiftFromSchedule(shiftId: Int) =
        safeApiCall({ api.removeShiftFromSchedule(shiftId) }) { it?.success }

    override suspend fun getSchedulingPeriods(from: ZonedDateTime?) =
        safeApiCall({ api.getSchedulingPeriods(from?.toString()) }) { it?.periods }

    override suspend fun getSchedulingPeriod(id: Int) =
        safeApiCall({ api.getSchedulingPeriod(id) }) { it }

    override suspend fun getUpcomingPeriod() =
        safeApiCall({ api.getUpcomingPeriod() }) { it }

    override suspend fun getSchedulingUnits(periodId: Int) =
        safeApiCall({ api.getSchedulingUnits(periodId) }) { it?.data }

    override suspend fun addShiftTemplate(template: ShiftTemplate) =
        safeApiCall({ api.createTemplate(template) }) { it?.data }

    override suspend fun getShiftTemplates(unitId: Int) =
        safeApiCall({
            api.getShiftTemplates(unitId)
        }) { it?.data }

    override suspend fun getTemplateEmployees(templateId: Int, page: Int) =
        safeApiCall({
            api.getTemplateEmployees(templateId, page)
        }) { it }

    override suspend fun getOrganizationEmployees(workingNow: Boolean, page: Int) =
        safeApiCall({
            api.getOrganizationEmployees(workingNow, page)
        }) { it }

    override suspend fun getSpecializationEmployees(id: Int, page: Int) =
        safeApiCall({
            api.getSpecializationEmployees(id, page)
        }) { it }

    override suspend fun getSpecializationEmployeesPossibilities(id: Int) =
        safeApiCall({
            api.getSpecializationEmployeesPossibilities(id)
        }) { it?.data }

    override suspend fun putSpecializationEmployees(
        periodId: Int,
        request: UpdateSpecializationsRequest
    ) = safeApiCall({
        api.putSpecializationEmployees(periodId, request)
    }) { true }

    override suspend fun updateDemand(templateId: Int, priority: Int) =
        safeApiCall({
            api.updateDemand(templateId, priority)
        }) { true }

    override suspend fun getSpecializations(forTemplateId: Int?) =
        safeApiCall({
            api.getSpecializations(forTemplateId)
        }) { it?.data }

    override suspend fun getEmployeeSpecializations(employeeId: Int) =
        safeApiCall({
            api.getEmployeeSpecializations(employeeId)
        }) { it?.data }

    override suspend fun createSpecializedShift(
        templateId: Int,
        specializationId: Int
    ) = safeApiCall({
        api.createSpecializedShift(templateId, specializationId)
    }) { it }

    override suspend fun createSpecialization(data: CreateSpecializationRequest) =
        safeApiCall({
            api.createSpecialization(data)
        }) { true }

    override suspend fun getEmployeeShifts(id: Int) =
        safeApiCall({
            api.getEmployeeShifts(id)
        }) { it?.data }

    override suspend fun getPeriodDays(periodId: Int) =
        safeApiCall({
            api.getPeriodDays(periodId)
        }) { it?.days }

    override suspend fun getShiftTimeCalculations(
        periodId: Int,
        startTime: String,
        endTime: String,
        shiftHours: Int,
        breakMinutes: Int,
        perDay: Int,
        nightShift: Boolean,
        is24Hours: Boolean,
        shiftStart: String?
    ) =
        safeApiCall({
            api.getShiftTimeCalculations(
                periodId,
                startTime,
                endTime,
                shiftHours,
                breakMinutes,
                perDay,
                nightShift,
                is24Hours,
                shiftStart
            )
        }) { it?.times }

    override suspend fun createShiftTemplates(
        periodId: Int,
        request: CreateShiftTemplatesRequest
    ) = safeApiCall({
        api.createShiftTemplates(
            periodId,
            request
        )
    }) { it?.data}

    override suspend fun callAutoScheduler(periodId: Int, schedulingParams: SchedulingParams) = safeApiCall(
        { api.callAutoSchedule(periodId, schedulingParams) }
    ) { it?.success }

    override suspend fun submitSchedule(periodId: Int) =
        safeApiCall({
            api.submitSchedule(periodId)
        }) { it?.data }

    private fun Headers.saveUserHeaders() =
        Constants.UserHeaders.values().forEach { getAndSave(it.key) }

    private fun Headers.getAndSave(key: String) = get(key)?.let { PrefsUtils.putString(key, it) }
}
