package cz.cvut.fel.kopecm26.bakaplanner.networking.modules

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ContractResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodDaysResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ScheduleResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplatesResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTimeCalculationResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.AutoSchedulerResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.CreateContractResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.SubmitScheduleResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateContractRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateShiftTemplatesRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.SubmitScheduleRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @POST("/contracts")
    suspend fun createContract(@Body request: CreateContractRequest): Response<CreateContractResponse>

    @GET("/employees/{id}/contracts")
    suspend fun getEmployeeContracts(@Path("id") employeeId: Int): Response<ContractResponse>

    @GET("/shift/{id}/schedules")
    suspend fun getSchedulesForShift(@Path("id") shiftId: Int): Response<ScheduleResponse>

    @POST("/shifts")
    suspend fun addShiftToSchedule(
        @Query("template_id") shiftId: Int,
        @Query("schedule_id") scheduleId: Int
    ): Response<Shift>

    @DELETE("/shift/{id}/schedule")
    suspend fun removeShiftFromSchedule(@Path("id") shiftId: Int): Response<Shift>

    // TODO not optimal use of REST API
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
        @Query("night_shift") nightShift: Boolean,
        @Query("is_24_hours") is24Hours: Boolean,
        @Query("shift_start") shiftStart: String?
    ): Response<ShiftTimeCalculationResponse>

    @POST("/periods/{id}/templates")
    suspend fun createShiftTemplates(
        @Path("id") periodId: Int,
        @Body body: CreateShiftTemplatesRequest,
    ): Response<ShiftTemplatesResponse>

    // TODO not optimal use of REST API
    @POST("/periods/{id}/calculations/generate-schedule")
    suspend fun callAutoSchedule(
        @Path("id") periodId: Int
    ): Response<AutoSchedulerResponse>

    @PUT("/periods/{id}")
    suspend fun submitSchedule(
        @Path("id") periodId: Int,
        @Body request: SubmitScheduleRequest = SubmitScheduleRequest(true)
    ): Response<SubmitScheduleResponse>

    @PATCH("templates/{id}")
    suspend fun updateDemand(@Path("id") templateId: Int, @Query("priority") priority: Int): Response<Void>

    @POST("/templates/{id}/specialized")
    suspend fun createSpecializedShift(@Path("id") templateId: Int, @Query("specialization_id") specializationId: Int): Response<ShiftTemplate>
}
