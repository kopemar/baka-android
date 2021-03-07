package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate

class PlanningRepository(private val service: RemoteDataSource) {
    suspend fun getSchedulingPeriods() = service.getSchedulingPeriods()

    suspend fun getSchedulingUnits(periodId: Int) = service.getSchedulingUnits(periodId)

    suspend fun addShiftTemplate(template: ShiftTemplate) = service.addShiftTemplate(template)

    suspend fun getShiftTemplates(unitId: Int) = service.getShiftTemplates(unitId)

    suspend fun getShiftTemplateEmployees(templateId: Int) = service.getTemplateEmployees(templateId)

    suspend fun getShiftTimeCalculations(
        periodId: Int,
        startTime: String,
        endTime: String,
        shiftHours: Int,
        breakMinutes: Int,
        perDay: Int,
    ) = service.getShiftTimeCalculations(
        periodId,
        startTime,
        endTime,
        shiftHours,
        breakMinutes,
        perDay
    )

    suspend fun createShiftTemplates(
        periodId: Int,
        startTime: String,
        endTime: String,
        shiftHours: Int,
        breakMinutes: Int,
        perDay: Int,
        excluded: Map<Int, ArrayList<Int>>,
        workingDays: List<Int>
    ) = service.createShiftTemplates(periodId, startTime, endTime, shiftHours, breakMinutes, perDay, excluded, workingDays)

    suspend fun getPeriodDays(periodId: Int) = service.getPeriodDays(periodId)

    suspend fun callAutoScheduler(periodId: Int) = service.callAutoScheduler(periodId)
}
