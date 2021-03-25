package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Priority
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Specialization
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateSpecializationRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.UpdateSpecializationsRequest

class SpecializationRepository(private val service: RemoteDataSource) {
    suspend fun fetchSpecializations() =
        service.getOrganizationSpecializations()

    suspend fun createSpecialization(name: String) =
        service.createSpecialization(CreateSpecializationRequest(name))

    suspend fun getSpecializationEmployees(id: Int) = service.getSpecializationEmployees(id)

    suspend fun getSpecializationEmployeesPossibilities(id: Int) = service.getSpecializationEmployeesPossibilities(id)

    suspend fun putSpecializationEmployees(periodId: Int, request: List<Int>) = service.putSpecializationEmployees(periodId, UpdateSpecializationsRequest(request))

    suspend fun updateDemand(templateId: Int, priority: Priority) = service.updateDemand(templateId, priority.integerValue)

    suspend fun getPossibleSpecializations(templateId: Int) = service.getPossibleTemplateSpecializations(templateId)

    suspend fun createSpecializedShift(templateId: Int, specialization: Specialization) = service.createSpecializedShift(templateId, specialization.id)
}
