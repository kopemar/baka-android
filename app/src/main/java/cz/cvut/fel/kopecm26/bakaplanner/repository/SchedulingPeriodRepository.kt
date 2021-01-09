package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource

class SchedulingPeriodRepository(private val service: RemoteDataSource) {
    suspend fun getSchedulingPeriods() = service.getSchedulingPeriods()
}