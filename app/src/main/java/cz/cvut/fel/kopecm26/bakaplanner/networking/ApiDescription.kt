package cz.cvut.fel.kopecm26.bakaplanner.networking

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiDescription {
    // AUTH
    @POST("/auth/sign_in")
    suspend fun signIn(@Body auth: Auth): Response<UserResponseModel>

    @DELETE("/auth/sign_out")
    suspend fun signOut(): Response<SignOutModel>

    @GET("/shifts")
    suspend fun getShifts(): Response<ShiftResponse>

    @GET("/shifts")
    suspend fun getShifts(@Query("start_date") startDate: String, @Query("end_date") endDate: String): Response<ShiftResponse>

    @GET("/shifts")
    suspend fun getShiftsBefore(@Query("end_date") endDate: String): Response<ShiftResponse>

    @GET("/shifts?unassigned=true")
    suspend fun getUnassignedShiftsFrom(@Query("start_date") startDate: String): Response<ShiftResponse>

    @GET("/contracts")
    suspend fun getContracts(): Response<ContractResponse>

    @GET("/shift/{id}/schedules")
    suspend fun getSchedulesForShift(@Path("id") shiftId: Int): Response<ScheduleResponse>

    @POST("/shift/{id}/schedule")
    suspend fun addShiftToSchedule(@Path("id") shiftId: Int, @Query("schedule_id") scheduleId: Int): Response<Shift>

    @DELETE("/shift/{id}/schedule")
    suspend fun removeShiftFromSchedule(@Path("id") shiftId: Int): Response<Shift>

    /**
     * Scheduling periods, usually weeks
     */
    @GET("/periods")
    suspend fun getSchedulingPeriods(): Response<SchedulingPeriodsResponse>
}