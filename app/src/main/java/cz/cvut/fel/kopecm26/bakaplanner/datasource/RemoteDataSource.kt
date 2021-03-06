package cz.cvut.fel.kopecm26.bakaplanner.datasource

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Auth
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Contract
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodDay
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Schedule
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingUnit
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTimeCalculation
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SignOutModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Specialization
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.User
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.CreateContractResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.CreateEmployeeResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.CreateOrganizationResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.EmployeeListResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.EmployeePresenter
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.AddShiftToSchedules
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateContractRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateEmployeeRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateOrganizationRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateShiftTemplatesRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateSpecializationRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.SchedulingParams
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.UpdateSpecializationsRequest
import java.time.ZonedDateTime

interface RemoteDataSource {

    suspend fun signIn(auth: Auth): ResponseModel<User>

    suspend fun signOut(): ResponseModel<SignOutModel>

    suspend fun signUp(request: CreateOrganizationRequest): ResponseModel<CreateOrganizationResponse>

    suspend fun createEmployee(request: CreateEmployeeRequest): ResponseModel<CreateEmployeeResponse>

    suspend fun postFirebaseToken(token: String): ResponseModel<Boolean>

    suspend fun getShifts(): ResponseModel<List<Shift>>

    suspend fun getShiftsBefore(to: ZonedDateTime): ResponseModel<List<Shift>>

    suspend fun getUnassignedShifts(from: ZonedDateTime = ZonedDateTime.now()): ResponseModel<List<ShiftTemplate>>

    suspend fun getContracts(): ResponseModel<List<Contract>>

    suspend fun getEmployeeContracts(employeeId: Int): ResponseModel<List<Contract>>

    suspend fun createContract(employeeId: Int, request: CreateContractRequest): ResponseModel<CreateContractResponse>

    suspend fun getSchedulesForShift(shiftId: Int): ResponseModel<List<Schedule>>

    suspend fun addShiftToSchedule(shiftId: Int, scheduleId: Int): ResponseModel<Shift>

    suspend fun addShiftToSchedules(shiftId: Int, request: AddShiftToSchedules): ResponseModel<List<Shift>>

    suspend fun removeShiftFromSchedule(shiftId: Int): ResponseModel<Boolean>

    suspend fun getSchedulingPeriods(from: ZonedDateTime? = null): ResponseModel<List<SchedulingPeriod>>

    suspend fun getSchedulingPeriod(id: Int): ResponseModel<SchedulingPeriod>

    suspend fun getUpcomingPeriod(): ResponseModel<SchedulingPeriod>

    suspend fun getSchedulingUnits(periodId: Int): ResponseModel<List<SchedulingUnit>>

    suspend fun addShiftTemplate(template: ShiftTemplate): ResponseModel<ShiftTemplate>

    suspend fun getShiftTemplates(unitId: Int): ResponseModel<List<ShiftTemplate>>

    suspend fun getTemplateEmployees(templateId: Int, page: Int = 1): ResponseModel<EmployeeListResponse>

    suspend fun getOrganizationEmployees(workingNow: Boolean = false, page: Int = 1): ResponseModel<EmployeeListResponse>

    suspend fun getSpecializationEmployees(id: Int, page: Int = 1): ResponseModel<EmployeeListResponse>

    suspend fun getSpecializationEmployeesPossibilities(id: Int): ResponseModel<List<EmployeePresenter>>

    suspend fun putSpecializationEmployees(
        periodId: Int,
        request: UpdateSpecializationsRequest
    ): ResponseModel<Boolean>

    suspend fun updateDemand(templateId: Int, priority: Int): ResponseModel<Boolean>

    suspend fun getSpecializations(forTemplateId: Int? = null): ResponseModel<List<Specialization>>

    suspend fun getEmployeeSpecializations(employeeId: Int): ResponseModel<List<Specialization>>

    suspend fun createSpecializedShift(
        templateId: Int,
        specializationId: Int
    ): ResponseModel<ShiftTemplate>

    suspend fun createSpecialization(data: CreateSpecializationRequest): ResponseModel<Boolean>

    suspend fun getEmployeeShifts(id: Int): ResponseModel<List<Shift>>

    suspend fun getPeriodDays(periodId: Int): ResponseModel<List<PeriodDay>>

    suspend fun getShiftTimeCalculations(
        periodId: Int,
        startTime: String,
        endTime: String,
        shiftHours: Int,
        breakMinutes: Int,
        perDay: Int,
        nightShift: Boolean = false,
        is24Hours: Boolean = false,
        shiftStart: String? = null
    ): ResponseModel<List<ShiftTimeCalculation>>

    suspend fun createShiftTemplates(
        periodId: Int,
        request: CreateShiftTemplatesRequest
    ): ResponseModel<List<ShiftTemplate>>

    suspend fun callAutoScheduler(
        periodId: Int,
        schedulingParams: SchedulingParams
    ): ResponseModel<Boolean>

    suspend fun submitSchedule(periodId: Int): ResponseModel<SchedulingPeriod>
}
