package cz.cvut.fel.kopecm26.bakaplanner.datasource

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.*
import java.time.ZonedDateTime

interface RemoteDataSource {

    suspend fun signIn(auth: Auth): ResponseModel<User>

    suspend fun signOut(): ResponseModel<SignOutModel>

    suspend fun getShifts(from: ZonedDateTime, to: ZonedDateTime): ResponseModel<List<Shift>>

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

}