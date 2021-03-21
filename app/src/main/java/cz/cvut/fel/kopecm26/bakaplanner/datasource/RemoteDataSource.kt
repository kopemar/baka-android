package cz.cvut.fel.kopecm26.bakaplanner.datasource

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Auth
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Contract
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee
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
import java.time.ZonedDateTime

interface RemoteDataSource {

    suspend fun signIn(auth: Auth): ResponseModel<User>

    suspend fun signOut(): ResponseModel<SignOutModel>

    suspend fun postFirebaseToken(token: String): ResponseModel<Boolean>

    suspend fun getShifts(): ResponseModel<List<Shift>>

    suspend fun getShiftsBefore(to: ZonedDateTime): ResponseModel<List<Shift>>

    suspend fun getUnassignedShifts(from: ZonedDateTime = ZonedDateTime.now()): ResponseModel<List<ShiftTemplate>>

    suspend fun getContracts(): ResponseModel<List<Contract>>

    suspend fun getSchedulesForShift(shiftId: Int): ResponseModel<List<Schedule>>

    suspend fun addShiftToSchedule(shiftId: Int, scheduleId: Int): ResponseModel<Shift>

    suspend fun removeShiftFromSchedule(shiftId: Int): ResponseModel<Shift>

    suspend fun getSchedulingPeriods(): ResponseModel<List<SchedulingPeriod>>

    suspend fun getSchedulingUnits(periodId: Int): ResponseModel<List<SchedulingUnit>>

    suspend fun addShiftTemplate(template: ShiftTemplate): ResponseModel<ShiftTemplate>

    suspend fun getShiftTemplates(unitId: Int): ResponseModel<List<ShiftTemplate>>

    suspend fun getTemplateEmployees(templateId: Int): ResponseModel<List<Employee>>

    suspend fun getOrganizationEmployees(id: Int): ResponseModel<List<Employee>>
    suspend fun getOrganizationSpecializations(): ResponseModel<List<Specialization>>

    suspend fun getEmployeeShifts(id: Int): ResponseModel<List<Shift>>

    suspend fun getPeriodDays(periodId: Int): ResponseModel<List<PeriodDay>>

    suspend fun getShiftTimeCalculations(
        periodId: Int,
        startTime: String,
        endTime: String,
        shiftHours: Int,
        breakMinutes: Int,
        perDay: Int,
    ): ResponseModel<List<ShiftTimeCalculation>>

    suspend fun createShiftTemplates(
        periodId: Int,
        startTime: String,
        endTime: String,
        shiftHours: Int,
        breakMinutes: Int,
        perDay: Int,
        excluded: Map<Int, ArrayList<Int>>,
        workingDays: List<Int>
    ): ResponseModel<List<ShiftTemplate>>

    suspend fun callAutoScheduler(
        periodId: Int
    ): ResponseModel<Boolean>

    suspend fun submitSchedule(
        periodId: Int
    ): ResponseModel<SchedulingPeriod>
}
