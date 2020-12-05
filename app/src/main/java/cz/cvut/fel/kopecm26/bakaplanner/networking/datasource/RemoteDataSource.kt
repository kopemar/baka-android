package cz.cvut.fel.kopecm26.bakaplanner.networking.datasource

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Auth
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SignOutModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.User

interface RemoteDataSource {

    suspend fun signIn(auth: Auth) : ResponseModel<User>

    suspend fun signOut(): ResponseModel<SignOutModel>
}