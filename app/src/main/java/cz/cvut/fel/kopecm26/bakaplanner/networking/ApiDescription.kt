package cz.cvut.fel.kopecm26.bakaplanner.networking

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Auth
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SignOutModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.UserResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiDescription {
    @POST("/auth/sign_in")
    suspend fun signIn(@Body auth: Auth): Response<UserResponseModel>

    @DELETE("/auth/sign_out")
    suspend fun signOut(): Response<SignOutModel>

    @GET("/schedule")
    suspend fun getSchedule(): Response<ShiftResponse>
}