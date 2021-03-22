package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateSpecializationRequest

class OrganizationRepository(private val service: RemoteDataSource) {
    suspend fun fetchSpecializations() =
        service.getOrganizationSpecializations()

    suspend fun createSpecialization(name: String) =
        service.createSpecialization(CreateSpecializationRequest(name))
}
