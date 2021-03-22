package cz.cvut.fel.kopecm26.bakaplanner.networking.modules

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ContractResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodDaysResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ScheduleResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplatesResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTimeCalculationResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.AutoSchedulerResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.SubmitScheduleResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateShiftTemplatesRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ShiftsApiModule {
    @GET("/shifts")
    suspend fun getShifts(): Response<ShiftResponse>

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

    @POST("/periods/{id}/shift-templates")
    suspend fun createShiftTemplates(
        @Path("id") periodId: Int,
        @Body body: CreateShiftTemplatesRequest,
    ): Response<ShiftTemplatesResponse>

    @POST("/periods/{id}/calculations/generate-schedule")
    suspend fun callAutoSchedule(
        @Path("id") periodId: Int
    ): Response<AutoSchedulerResponse>

    @POST("/periods/{id}/submit")
    suspend fun submitSchedule(
        @Path("id") periodId: Int
    ): Response<SubmitScheduleResponse>
}
