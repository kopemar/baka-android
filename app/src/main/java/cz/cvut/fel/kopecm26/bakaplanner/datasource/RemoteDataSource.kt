package cz.cvut.fel.kopecm26.bakaplanner.datasource

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.*

interface RemoteDataSource {

    suspend fun signIn(auth: Auth) : ResponseModel<User>

    suspend fun signOut(): ResponseModel<SignOutModel>

    suspend fun getShifts(): ResponseModel<List<Shift>>
}