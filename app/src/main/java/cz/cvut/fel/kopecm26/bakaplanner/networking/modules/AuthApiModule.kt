package cz.cvut.fel.kopecm26.bakaplanner.networking.modules

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Auth
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SignOutModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.UserResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.CreateEmployeeResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.CreateOrganizationResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateEmployeeRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateOrganizationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApiModule {
    @POST("/api/v1/auth/sign_in")
    suspend fun signIn(@Body auth: Auth): Response<UserResponseModel>

    @DELETE("/api/v1/auth/sign_out")
    suspend fun signOut(): Response<SignOutModel>

    @POST("/api/v1/users/fcm-token")
    suspend fun postFirebaseToken(@Query("fcm_token") fcmToken: String): Response<Void>

    @POST("/api/v1/organization")
    suspend fun postOrganization(@Body request: CreateOrganizationRequest): Response<CreateOrganizationResponse>

    @POST("/api/v1/employees")
    suspend fun postEmployee(@Body request: CreateEmployeeRequest): Response<CreateEmployeeResponse>
}
