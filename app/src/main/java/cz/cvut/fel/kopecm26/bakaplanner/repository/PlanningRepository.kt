package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource

class PlanningRepository(private val service: RemoteDataSource) {
    suspend fun getSchedulingPeriods() = service.getSchedulingPeriods()

    suspend fun getSchedulingUnits(periodId: Int) = service.getSchedulingUnits(periodId)
}