package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ContractTypes
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateContractRequest

class ContractRepository(
    private val service: RemoteDataSource
) {

    suspend fun getContracts() = service.getContracts()

    suspend fun getEmployeeContracts(employeeId: Int) = service.getEmployeeContracts(employeeId)

    suspend fun createContract(
        employeeId: Int,
        startDate: String,
        endDate: String?,
        workLoad: Float?,
        type: ContractTypes
    ) = service.createContract(
        employeeId,
        CreateContractRequest(
            employeeId,
            startDate,
            endDate,
            workLoad,
            type.type
        )
    )
}
