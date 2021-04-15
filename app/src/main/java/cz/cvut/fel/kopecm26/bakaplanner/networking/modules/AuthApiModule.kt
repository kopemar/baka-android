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
    @POST("/auth/sign_in")
    suspend fun signIn(@Body auth: Auth): Response<UserResponseModel>

    @DELETE("/auth/sign_out")
    suspend fun signOut(): Response<SignOutModel>

    @POST("/users/fcm-token")
    suspend fun postFirebaseToken(@Query("fcm_token") fcmToken: String): Response<Void>

    @POST("/organization")
    suspend fun postOrganization(@Body request: CreateOrganizationRequest): Response<CreateOrganizationResponse>

    @POST("/employee")
    suspend fun postEmployee(@Body request: CreateEmployeeRequest): Response<CreateEmployeeResponse>
}
