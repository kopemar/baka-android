package cz.cvut.fel.kopecm26.bakaplanner.repository

import com.pixplicity.easyprefs.library.Prefs
import cz.cvut.fel.kopecm26.bakaplanner.networking.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Auth
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.User
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants

class UserRepository(private val service: RemoteDataSource) {
    suspend fun signIn(username: String, password: String): User? = service.signIn(Auth(username, password))

    suspend fun signOut(): Boolean {
        val result = service.signOut()
        if (result) {
            Constants.UserHeaders.values().forEach { Prefs.remove(it.key) }
            Prefs.remove(Constants.Prefs.USER)
        }
        return result
    }
}