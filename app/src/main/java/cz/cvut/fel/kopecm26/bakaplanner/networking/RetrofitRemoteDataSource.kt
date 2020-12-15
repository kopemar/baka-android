package cz.cvut.fel.kopecm26.bakaplanner.networking

import com.pixplicity.easyprefs.library.Prefs
import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.*
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.weeksAfter
import cz.cvut.fel.kopecm26.bakaplanner.util.networking.safeApiCall
import okhttp3.Headers
import java.time.LocalDate

class RetrofitRemoteDataSource(private val api: ApiDescription) : RemoteDataSource {
    override suspend fun signIn(auth: Auth): ResponseModel<User> {
        val response = safeApiCall({ api.signIn(auth) }, { it?.user })

        if (response is ResponseModel.SUCCESS<User>) response.headers?.saveUserHeaders()
        return response
    }

    override suspend fun signOut() = safeApiCall({ api.signOut() }, { it })

    override suspend fun getShifts(): ResponseModel<List<Shift>> =
        safeApiCall(
            { api.getShifts(LocalDate.now(), LocalDate.now().weeksAfter(1)) },
            { it?.shifts })

    override suspend fun getContracts(): ResponseModel<List<Contract>> =
        safeApiCall({ api.getContracts() }, { it?.contracts })

    private fun Headers.saveUserHeaders() =
        Constants.UserHeaders.values().forEach { getAndSave(it.key) }

    private fun Headers.getAndSave(key: String) = get(key)?.let { Prefs.putString(key, it) }
}
