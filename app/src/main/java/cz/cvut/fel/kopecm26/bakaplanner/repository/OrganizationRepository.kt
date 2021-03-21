package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource

class OrganizationRepository(private val service: RemoteDataSource) {
    suspend fun fetchSpecializations() =
        service.getOrganizationSpecializations()
}
