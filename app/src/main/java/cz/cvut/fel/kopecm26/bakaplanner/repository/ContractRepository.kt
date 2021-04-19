package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.db.dao.ContractDao

class ContractRepository(
    private val service: RemoteDataSource,
    private val contractDao: ContractDao
) {

    suspend fun deleteAll() {
        contractDao.deleteAll()
    }

    suspend fun getContracts() = service.getContracts()

    suspend fun getEmployeeContracts(employeeId: Int) = service.getEmployeeContracts(employeeId)
}
