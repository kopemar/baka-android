package cz.cvut.fel.kopecm26.bakaplanner.networking.modules

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriodsResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingUnitsResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplateResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplatesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SchedulingApiModule {
    /**
     * Scheduling periods, usually weeks
     */
    @GET("/api/v1/periods")
    suspend fun getSchedulingPeriods(@Query("from") from: String? = null): Response<SchedulingPeriodsResponse>

    @GET("/api/v1/periods/upcoming")
    suspend fun getUpcomingPeriod(): Response<SchedulingPeriod>

    @GET("/api/v1/periods/{id}/units")
    suspend fun getSchedulingUnits(@Path("id") periodId: Int): Response<SchedulingUnitsResponse>

    @GET("/api/v1/periods/{id}")
    suspend fun getSchedulingPeriod(@Path("id") periodId: Int): Response<SchedulingPeriod>

    @POST("/api/v1/templates")
    suspend fun createTemplate(@Body template: ShiftTemplate): Response<ShiftTemplateResponse>

    @GET("/api/v1/templates")
    suspend fun getShiftTemplates(@Query("unit") unitId: Int? = null): Response<ShiftTemplatesResponse>
}
