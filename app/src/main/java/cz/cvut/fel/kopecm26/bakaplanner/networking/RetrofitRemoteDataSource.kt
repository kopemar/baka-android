package cz.cvut.fel.kopecm26.bakaplanner.networking

import com.pixplicity.easyprefs.library.Prefs
import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Auth
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.User
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
import cz.cvut.fel.kopecm26.bakaplanner.util.networking.safeApiCall
import okhttp3.Headers
import java.time.ZonedDateTime

class RetrofitRemoteDataSource(private val api: ApiDescription) : RemoteDataSource {
    override suspend fun signIn(auth: Auth): ResponseModel<User> {
        val response = safeApiCall({ api.signIn(auth) }, { it?.user })

        if (response is ResponseModel.SUCCESS<User>) response.headers?.saveUserHeaders()
        return response
    }

    override suspend fun signOut() = safeApiCall({ api.signOut() }, { it })

    override suspend fun getShifts(from: ZonedDateTime, to: ZonedDateTime) = safeApiCall(
        { api.getShifts(from.toString(), to.toString()) },
        { it?.shifts })

    override suspend fun getShiftsBefore(to: ZonedDateTime) = safeApiCall(
        { api.getShiftsBefore(to.toString()) },
        { it?.shifts })

    override suspend fun getUnassignedShifts(from: ZonedDateTime) = safeApiCall(
        { api.getUnassignedShiftsFrom(from.toString()) },
        { it?.shifts })


    override suspend fun getContracts() = safeApiCall({ api.getContracts() }, { it?.contracts })

    private fun Headers.saveUserHeaders() =
        Constants.UserHeaders.values().forEach { getAndSave(it.key) }

    private fun Headers.getAndSave(key: String) = get(key)?.let { Prefs.putString(key, it) }
}
