package cz.cvut.fel.kopecm26.bakaplanner.networking

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Auth
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftResponse
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SignOutModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.UserResponseModel
import retrofit2.Response
import retrofit2.http.*
import java.time.LocalDate

interface ApiDescription {
    @POST("/auth/sign_in")
    suspend fun signIn(@Body auth: Auth): Response<UserResponseModel>

    @DELETE("/auth/sign_out")
    suspend fun signOut(): Response<SignOutModel>

    @GET("/shifts")
    suspend fun getShifts(): Response<ShiftResponse>

    @GET("/shifts")
    suspend fun getShifts(@Query("start_date") startDate: LocalDate, @Query("end_date") endDate: LocalDate): Response<ShiftResponse>
}