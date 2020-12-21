package cz.cvut.fel.kopecm26.bakaplanner.datasource

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.*
import java.time.LocalDate

interface RemoteDataSource {

    suspend fun signIn(auth: Auth): ResponseModel<User>

    suspend fun signOut(): ResponseModel<SignOutModel>

    suspend fun getShifts(from: LocalDate, to: LocalDate): ResponseModel<List<Shift>>

    suspend fun getContracts(): ResponseModel<List<Contract>>
}