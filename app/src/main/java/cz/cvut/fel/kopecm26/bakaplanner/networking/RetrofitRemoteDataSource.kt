package cz.cvut.fel.kopecm26.bakaplanner.networking

import com.pixplicity.easyprefs.library.Prefs
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Auth
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.User
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
import okhttp3.Headers
import retrofit2.HttpException
import java.io.IOException

class RetrofitRemoteDataSource(val api: ApiDescription) : RemoteDataSource {
    override suspend fun signIn(auth: Auth): User? = try {
        api.signIn(auth).let {
            if (it.isSuccessful)
                it.body()?.user?.apply {
                    it.headers().saveUserHeaders()
                }
            else throw HttpException(it)
        }
    } catch (e: Exception) {
        throw mapToDomainException(e) {
            if (it.code() == 401) {
                UnauthorizedException()
            } else {
                null
            }
        }
    }

    override suspend fun signOut(): Boolean = try {
        api.signOut().body()?.success ?: false
    } catch (e: Exception) {
        false
    }


    private fun mapToDomainException(
        remoteException: Exception,
        httpExceptionsMapper: (HttpException) -> Exception? = { null }
    ): Exception {
        return when (remoteException) {
            is IOException -> NoInternetException()
            is HttpException -> httpExceptionsMapper(remoteException) ?: ApiException(remoteException.code().toString())
            else -> UnexpectedException(remoteException)
        }
    }

    private fun Headers.saveUserHeaders() = Constants.UserHeaders.values().forEach { getAndSave(it.key) }

    private fun Headers.getAndSave(key: String) = get(key)?.let { Prefs.putString(key, it) }
}


/**
 * Exception when communicating with the remote api. Contains http [statusCode].
 */
data class ApiException(val statusCode: String) : Exception()

/**
 * Exception indicating that device is not connected to the internet
 */
class NoInternetException : Exception()

/**
 * Not handled unexpected exception
 */
class UnexpectedException(cause: Exception) : Exception(cause)

/**
 * Exception indicating that user is not authorized
 */
class UnauthorizedException : Exception()