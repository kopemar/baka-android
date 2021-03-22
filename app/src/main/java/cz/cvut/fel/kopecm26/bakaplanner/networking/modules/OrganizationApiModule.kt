package cz.cvut.fel.kopecm26.bakaplanner.networking.modules

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.EmployeeListResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.EmployeeShiftsResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.SpecializationsResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateSpecializationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OrganizationApiModule {
    @GET("/templates/{id}/employees")
    suspend fun getTemplateEmployees(@Path("id") id: Int): Response<EmployeeListResponse>

    @GET("/organization/{id}/employees")
    suspend fun getOrganizationEmployees(@Path("id") periodId: Int): Response<EmployeeListResponse>

    @GET("/specializations")
    suspend fun getOrganizationSpecializations(): Response<SpecializationsResponse>

    @POST("/specializations")
    suspend fun createSpecialization(@Body data: CreateSpecializationRequest): Response<Void>

    @GET("/employees/{id}")
    suspend fun getEmployeeById(@Path("id") periodId: Int): Response<EmployeeListResponse>

    @GET("/employees/{id}/shifts")
    suspend fun getEmployeeShifts(
        @Path("id") employeeId: Int,
        @Query("upcoming") upcoming: Boolean = true
    ): Response<EmployeeShiftsResponse>
}
