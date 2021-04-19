package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateShiftTemplatesRequest

class PlanningRepository(private val service: RemoteDataSource) {
    suspend fun getSchedulingPeriods() = service.getSchedulingPeriods()

    suspend fun getSchedulingUnits(periodId: Int) = service.getSchedulingUnits(periodId)

    suspend fun addShiftTemplate(template: ShiftTemplate) = service.addShiftTemplate(template)

    suspend fun getShiftTemplates(unitId: Int) = service.getShiftTemplates(unitId)

    suspend fun getShiftTemplateEmployees(templateId: Int) =
        service.getTemplateEmployees(templateId)

    suspend fun getShiftTimeCalculations(
        periodId: Int,
        startTime: String,
        endTime: String,
        shiftHours: Int,
        breakMinutes: Int,
        perDay: Int,
        nightShift: Boolean,
        is24Hours: Boolean,
        shiftStart: String?
    ) = service.getShiftTimeCalculations(
        periodId,
        startTime,
        endTime,
        shiftHours,
        breakMinutes,
        perDay,
        nightShift,
        is24Hours,
        shiftStart
    )

    suspend fun createShiftTemplates(
        periodId: Int,
        startTime: String?,
        endTime: String?,
        shiftHours: Int,
        breakMinutes: Int,
        perDay: Int,
        excluded: Map<Int, ArrayList<Int>>,
        workingDays: List<Int>,
        nightShift: Boolean,
        is24Hours: Boolean,
        shiftStart: String?
    ) = service.createShiftTemplates(
        periodId,
        CreateShiftTemplatesRequest(
            startTime,
            endTime,
            shiftHours,
            breakMinutes,
            perDay,
            excluded,
            workingDays,
            nightShift,
            is24Hours,
            shiftStart
        )
    )

    suspend fun getPeriodDays(periodId: Int) = service.getPeriodDays(periodId)

    suspend fun callAutoScheduler(periodId: Int) = service.callAutoScheduler(periodId)

    suspend fun submitSchedule(periodId: Int) = service.submitSchedule(periodId)
}
