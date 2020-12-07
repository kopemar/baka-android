package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.datasource.dao.ShiftDao
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

class ShiftRepository(private val service: RemoteDataSource, private val shiftDao: ShiftDao) {

    suspend fun getShifts(forceUpdate: Boolean = false): ResponseModel<List<Shift>> {
        return if (forceUpdate || shiftDao.getAll().isNullOrEmpty()) {
            service.getShifts().apply {
                if (this is ResponseModel.SUCCESS) {
                    data?.forEach { shiftDao.insert(it) }
                }
            }
        } else {
            ResponseModel.SUCCESS(shiftDao.getAll())
        }

    }
}