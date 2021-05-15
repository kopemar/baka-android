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
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.DeleteShiftResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.ShiftsResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.SubmitScheduleResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.AddShiftToSchedules
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateContractRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateShiftTemplatesRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.SchedulingParams
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
    @GET("/api/v1/shifts")
    suspend fun getShifts(): Response<ShiftResponse>

    @GET("/api/v1/shifts")
    suspend fun getShiftsBefore(@Query("end_date") endDate: String): Response<ShiftResponse>

    @GET("/api/v1/templates")
    suspend fun getUnassignedShiftsFrom(@Query("start_date") startDate: String, @Query("unassigned") unassigned: Boolean = true): Response<ShiftTemplatesResponse>

    @GET("/api/v1/contracts")
    suspend fun getContracts(): Response<ContractResponse>

    @POST("/api/v1/contracts")
    suspend fun createContract(@Body request: CreateContractRequest): Response<CreateContractResponse>

    @GET("/api/v1/employees/{id}/contracts")
    suspend fun getEmployeeContracts(@Path("id") employeeId: Int): Response<ContractResponse>

    @GET("/api/v1/shift/{id}/schedules")
    suspend fun getSchedulesForShift(@Path("id") shiftId: Int): Response<ScheduleResponse>

    @POST("/api/v1/shifts")
    suspend fun addShiftToSchedule(
        @Query("template_id") shiftId: Int,
        @Query("schedule_id") scheduleId: Int
    ): Response<Shift>

    @POST("/api/v1/shifts")
    suspend fun addShiftToSchedules(
        @Query("template_id") shiftId: Int,
        @Body schedule: AddShiftToSchedules
    ): Response<ShiftsResponse>

    @DELETE("/api/v1/shifts/{id}")
    suspend fun removeShiftFromSchedule(@Path("id") shiftId: Int): Response<DeleteShiftResponse>

    @GET("/api/v1/periods/{id}/calculate-period-days")
    suspend fun getPeriodDays(@Path("id") periodId: Int): Response<PeriodDaysResponse>

    @GET("/api/v1/periods/{id}/calculate-shift-times")
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

    @POST("/api/v1/periods/{id}/templates")
    suspend fun createShiftTemplates(
        @Path("id") periodId: Int,
        @Body body: CreateShiftTemplatesRequest,
    ): Response<ShiftTemplatesResponse>


    @POST("/api/v1/periods/{id}/generate-schedule")
    suspend fun callAutoSchedule(
        @Path("id") periodId: Int,
        @Body params: SchedulingParams
    ): Response<AutoSchedulerResponse>

    @PUT("/api/v1/periods/{id}")
    suspend fun submitSchedule(
        @Path("id") periodId: Int,
        @Body request: SubmitScheduleRequest = SubmitScheduleRequest(true)
    ): Response<SubmitScheduleResponse>

    @PATCH("/api/v1/templates/{id}")
    suspend fun updateDemand(@Path("id") templateId: Int, @Query("priority") priority: Int): Response<Void>

    @POST("/api/v1/templates/{id}/specialized")
    suspend fun createSpecializedShift(@Path("id") templateId: Int, @Query("specialization_id") specializationId: Int): Response<ShiftTemplate>
}
