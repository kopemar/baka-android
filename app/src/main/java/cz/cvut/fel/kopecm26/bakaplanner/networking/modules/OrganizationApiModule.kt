package cz.cvut.fel.kopecm26.bakaplanner.networking.modules

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.AddSpecializationResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.EmployeeListResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.ShiftsResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.SpecializationsResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateSpecializationRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.UpdateSpecializationsRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OrganizationApiModule {
    @GET("/api/v1/templates/{id}/employees")
    suspend fun getTemplateEmployees(@Path("id") id: Int): Response<EmployeeListResponse>

    @GET("/api/v1/employees")
    suspend fun getOrganizationEmployees(@Query("working_now") workingNow: Boolean = false, @Query("page") page: Int = 1): Response<EmployeeListResponse>

    @GET("/api/v1/specializations")
    suspend fun getSpecializations(@Query("for_template") templateId: Int? = null): Response<SpecializationsResponse>

    @GET("/api/v1/employees/{id}/specializations")
    suspend fun getEmployeeSpecializations(@Path("id") employeeId: Int): Response<SpecializationsResponse>

    @GET("/api/v1/specializations/{id}/employees")
    suspend fun getSpecializationEmployees(@Path("id") periodId: Int): Response<EmployeeListResponse>

    // TODO TODO will change API endpoint
    @GET("/api/v1/specializations/{id}/calculations/contracts")
    suspend fun getSpecializationEmployeesPossibilities(@Path("id") periodId: Int): Response<AddSpecializationResponse>

    @PUT("/api/v1/specializations/{id}")
    suspend fun putSpecializationEmployees(@Path("id") periodId: Int, @Body request: UpdateSpecializationsRequest): Response<Void>

    @POST("/api/v1/specializations")
    suspend fun createSpecialization(@Body data: CreateSpecializationRequest): Response<Void>

    @GET("/api/v1/employees/{id}")
    suspend fun getEmployeeById(@Path("id") periodId: Int): Response<EmployeeListResponse>

    @GET("/api/v1/employees/{id}/shifts")
    suspend fun getEmployeeShifts(
        @Path("id") employeeId: Int,
        @Query("upcoming") upcoming: Boolean = true
    ): Response<ShiftsResponse>
}
