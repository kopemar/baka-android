package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.networking.datasource.RemoteDataSource

class ShiftRepository(private val service: RemoteDataSource) {
    // todo save to local DB
    suspend fun getShifts() = service.getShifts()
}