package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Auth
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SignOutModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.User

class UserRepository(private val service: RemoteDataSource) {
    suspend fun signIn(username: String, password: String): ResponseModel<User> = service.signIn(Auth(username, password))

    suspend fun signOut(): ResponseModel<SignOutModel> = service.signOut()
}
