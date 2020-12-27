package cz.cvut.fel.kopecm26.bakaplanner.datasource

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.*
import java.time.ZonedDateTime

interface RemoteDataSource {

    suspend fun signIn(auth: Auth): ResponseModel<User>

    suspend fun signOut(): ResponseModel<SignOutModel>

    suspend fun getShifts(from: ZonedDateTime, to: ZonedDateTime): ResponseModel<List<Shift>>

    suspend fun getShiftsBefore(to: ZonedDateTime): ResponseModel<List<Shift>>

    suspend fun getUnassignedShifts(from: ZonedDateTime = ZonedDateTime.now()): ResponseModel<List<Shift>>

    suspend fun getContracts(): ResponseModel<List<Contract>>

    suspend fun getSchedulesForShift(shiftId: Int): ResponseModel<List<Schedule>>

    suspend fun addShiftToSchedule(scheduleId: Int, shiftId: Int): ResponseModel<Shift>

}