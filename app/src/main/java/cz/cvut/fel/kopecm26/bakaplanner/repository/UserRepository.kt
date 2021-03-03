package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Auth

class UserRepository(private val service: RemoteDataSource) {
    suspend fun signIn(username: String, password: String) = service.signIn(Auth(username, password))

    suspend fun signOut() = service.signOut()

    suspend fun getOrganizationEmployees(id: Int) = service.getOrganizationEmployees(id)
}
