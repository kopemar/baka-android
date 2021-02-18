package cz.cvut.fel.kopecm26.bakaplanner.networking

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.*
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateShiftTemplatesRequest
import retrofit2.Response
import retrofit2.http.*

@JvmSuppressWildcards
interface ApiDescription {
    // AUTH
    @POST("/auth/sign_in")
    suspend fun signIn(@Body auth: Auth): Response<UserResponseModel>

    @DELETE("/auth/sign_out")
    suspend fun signOut(): Response<SignOutModel>

    @GET("/shifts")
    suspend fun getShifts(): Response<ShiftResponse>

    @GET("/shifts")
    suspend fun getShifts(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Response<ShiftResponse>

    @GET("/shifts")
    suspend fun getShiftsBefore(@Query("end_date") endDate: String): Response<ShiftResponse>

    @GET("/templates")
    suspend fun getUnassignedShiftsFrom(@Query("start_date") startDate: String): Response<ShiftTemplatesResponse>

    @GET("/contracts")
    suspend fun getContracts(): Response<ContractResponse>

    @GET("/shift/{id}/schedules")
    suspend fun getSchedulesForShift(@Path("id") shiftId: Int): Response<ScheduleResponse>

    @POST("/shifts")
    suspend fun addShiftToSchedule(
        @Query("template_id") shiftId: Int,
        @Query("schedule_id") scheduleId: Int
    ): Response<Shift>

    @DELETE("/shift/{id}/schedule")
    suspend fun removeShiftFromSchedule(@Path("id") shiftId: Int): Response<Shift>

    /**
     * Scheduling periods, usually weeks
     */
    @GET("/periods")
    suspend fun getSchedulingPeriods(): Response<SchedulingPeriodsResponse>

    @GET("/units/{id}")
    suspend fun getSchedulingUnits(@Path("id") periodId: Int): Response<SchedulingUnitsResponse>

    @POST("/templates")
    suspend fun createTemplate(@Body template: ShiftTemplate): Response<ShiftTemplateResponse>

    @GET("/templates")
    suspend fun getShiftTemplates(@Query("unit_id") unitId: Int): Response<ShiftTemplatesResponse>


    /* Period planning */

    @GET("/periods/{id}/calculations/period-days")
    suspend fun getPeriodDays(@Path("id") periodId: Int): Response<PeriodDaysResponse>

    @GET("/periods/{id}/calculations/shift-times")
    suspend fun getShiftTimeCalculations(
        @Path("id") periodId: Int,
        @Query("start_time") startTime: String,
        @Query("end_time") endTime: String,
        @Query("shift_hours") shiftHours: Int,
        @Query("break_minutes") breakMinutes: Int,
        @Query("per_day") perDay: Int,
    ): Response<ShiftTimeCalculationResponse>

    // TODO how to post excluded and working days?
    @POST("/periods/{id}/shift-templates")
    suspend fun createShiftTemplates(
        @Path("id") periodId: Int,
        @Body body: CreateShiftTemplatesRequest,
    ): Response<ShiftTemplatesResponse>

}