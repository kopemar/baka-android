package cz.cvut.fel.kopecm26.bakaplanner.repository

import com.orhanobut.logger.Logger
import com.pixplicity.easyprefs.library.Prefs
import cz.cvut.fel.kopecm26.bakaplanner.networking.ApiService
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Auth
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.User
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
import okhttp3.Headers

class UserRepository(private val service: ApiService) {
    suspend fun signIn(username: String, password: String): User? {
        val response = service.signIn(Auth(username, password))
        Logger.d(response.code())
        response.headers().saveUserHeaders()
        return response.body()?.user
    }

    private fun Headers.saveUserHeaders() = Constants.UserHeaders.values().forEach { getAndSave(it.key) }

    private fun Headers.getAndSave(key: String) = get(key)?.let { Prefs.putString(key, it) }

}