package cz.cvut.fel.kopecm26.bakaplanner.repository

import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.networking.ApiService
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Auth
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.User
import retrofit2.awaitResponse

class UserRepository(private val service: ApiService) {
    suspend fun signIn(username: String, password: String): User? {
        val response = service.signIn(Auth(username, password))
        Logger.d(response.code())
        return response.body()?.user
    }
}