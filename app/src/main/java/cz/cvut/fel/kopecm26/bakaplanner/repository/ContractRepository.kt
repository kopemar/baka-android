package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.datasource.dao.ContractDao
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Contract
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel

class ContractRepository(
    private val service: RemoteDataSource,
    private val contractDao: ContractDao
) {

    suspend fun refreshAll() = service.getContracts().apply {
        if (this is ResponseModel.SUCCESS) {
            data?.let { contractDao.insert(it) }
        }
    }

    suspend fun getAllCached(): ResponseModel<List<Contract>> =
        if (contractDao.getAll().isNullOrEmpty()) {
            refreshAll()
        } else {
            ResponseModel.SUCCESS(contractDao.getAll())
        }
}