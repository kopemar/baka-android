package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Auth
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateEmployeeRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateOrganizationRequest
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import java.time.LocalDate

class UserRepository(private val service: RemoteDataSource) {
    suspend fun signIn(username: String, password: String) = service.signIn(Auth(username, password))

    suspend fun signOut() = service.signOut()

    suspend fun signUp(
        name: String,
        username: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
    ) = service.signUp(CreateOrganizationRequest(
        name, username, email, password, firstName, lastName
    ))

    suspend fun createEmployee(
        username: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        dateOfBirth: LocalDate
    ) = service.createEmployee(CreateEmployeeRequest(
        username, email, password, firstName, lastName, dateOfBirth.toString()
    ))

    fun getCurrentUser() = PrefsUtils.getUser()

    suspend fun getOrganizationEmployees(workingNow: Boolean = false) = service.getOrganizationEmployees(workingNow)

    suspend fun getEmployeeShifts(id: Int) = service.getEmployeeShifts(id)

    suspend fun postFirebaseToken(token: String) = service.postFirebaseToken(token)
}
