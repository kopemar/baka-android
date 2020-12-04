package cz.cvut.fel.kopecm26.bakaplanner.networking

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Auth
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.User

interface RemoteDataSource {

    suspend fun signIn(auth: Auth) : User?

    suspend fun signOut(): Boolean
}