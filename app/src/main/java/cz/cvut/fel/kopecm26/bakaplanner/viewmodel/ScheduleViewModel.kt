package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

class ScheduleViewModel: BaseViewModel() {

    suspend fun getShifts() = shiftRepository.getShifts()

}