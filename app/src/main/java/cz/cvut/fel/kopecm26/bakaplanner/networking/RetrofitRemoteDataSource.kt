package cz.cvut.fel.kopecm26.bakaplanner.networking

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Auth
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Contract
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodDay
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTimeCalculation
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Specialization
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.User
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.CreateEmployeeResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.CreateOrganizationResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.EmployeePresenter
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateEmployeeRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateOrganizationRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateShiftTemplatesRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateSpecializationRequest
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

    override suspend fun signUp(request: CreateOrganizationRequest): ResponseModel<CreateOrganizationResponse> =
        safeApiCall({
            api.postOrganization(request)
        }) { it }

    override suspend fun createEmployee(request: CreateEmployeeRequest): ResponseModel<CreateEmployeeResponse> =
        safeApiCall({
            api.postEmployee(request)
        }) { it }

    override suspend fun postFirebaseToken(token: String): ResponseModel<Boolean> = safeApiCall({
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

    override suspend fun getEmployeeContracts(employeeId: Int): ResponseModel<List<Contract>> =
        safeApiCall({ api.getEmployeeContracts(employeeId) }) { it?.data }

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
        }) { it?.data }

    override suspend fun getTemplateEmployees(templateId: Int): ResponseModel<List<Employee>> =
        safeApiCall({
            api.getTemplateEmployees(templateId)
        }) { it?.employees }

    override suspend fun getOrganizationEmployees(id: Int): ResponseModel<List<Employee>> =
        safeApiCall({
            api.getOrganizationEmployees(id)
        }) { it?.employees }

    override suspend fun getSpecializationEmployees(id: Int): ResponseModel<List<Employee>> =
        safeApiCall({
            api.getSpecializationEmployees(id)
        }) { it?.employees }

    override suspend fun getSpecializationEmployeesPossibilities(id: Int): ResponseModel<List<EmployeePresenter>> =
        safeApiCall({
            api.getSpecializationEmployeesPossibilities(id)
        }) { it?.data }

    override suspend fun putSpecializationEmployees(
        periodId: Int,
        request: UpdateSpecializationsRequest
    ): ResponseModel<Boolean> = safeApiCall({
        api.putSpecializationEmployees(periodId, request)
    }) { true }

    override suspend fun updateDemand(templateId: Int, priority: Int): ResponseModel<Boolean> =
        safeApiCall({
            api.updateDemand(templateId, priority)
        }) { true }

    override suspend fun getSpecializations(forTemplateId: Int?): ResponseModel<List<Specialization>> =
        safeApiCall({
            api.getSpecializations(forTemplateId)
        }) { it?.data }

    override suspend fun getEmployeeSpecializations(employeeId: Int): ResponseModel<List<Specialization>> =
        safeApiCall({
            api.getEmployeeSpecializations(employeeId)
        }) { it?.data }

    override suspend fun createSpecializedShift(
        templateId: Int,
        specializationId: Int
    ): ResponseModel<ShiftTemplate> = safeApiCall({
        api.createSpecializedShift(templateId, specializationId)
    }) { it }

    override suspend fun createSpecialization(data: CreateSpecializationRequest): ResponseModel<Boolean> =
        safeApiCall({
            api.createSpecialization(data)
        }) { true }

    override suspend fun getEmployeeShifts(id: Int): ResponseModel<List<Shift>> =
        safeApiCall({
            api.getEmployeeShifts(id)
        }) { it?.data }

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
        perDay: Int,
        nightShift: Boolean,
        is24Hours: Boolean,
        shiftStart: String?
    ): ResponseModel<List<ShiftTimeCalculation>> =
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
    ): ResponseModel<List<ShiftTemplate>> = safeApiCall({
        api.createShiftTemplates(
            periodId,
            request
        )
    }) { it?.data}

    // TODO better response model
    override suspend fun callAutoScheduler(periodId: Int): ResponseModel<Boolean> = safeApiCall(
        { api.callAutoSchedule(periodId) }
    ) { it?.success }

    override suspend fun submitSchedule(periodId: Int): ResponseModel<SchedulingPeriod> =
        safeApiCall({
            api.submitSchedule(periodId)
        }) { it?.data }

    private fun Headers.saveUserHeaders() =
        Constants.UserHeaders.values().forEach { getAndSave(it.key) }

    private fun Headers.getAndSave(key: String) = get(key)?.let { PrefsUtils.putString(key, it) }
}
