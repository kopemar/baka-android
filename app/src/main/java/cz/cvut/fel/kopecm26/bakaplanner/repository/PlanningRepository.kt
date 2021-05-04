package cz.cvut.fel.kopecm26.bakaplanner.repository

import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.CreateShiftTemplatesRequest
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.SchedulingParams
import cz.cvut.fel.kopecm26.bakaplanner.networking.request.SchedulingPriorities
import java.time.ZonedDateTime

class PlanningRepository(private val service: RemoteDataSource) {
    suspend fun getSchedulingPeriods(from: ZonedDateTime? = null) = service.getSchedulingPeriods(from)

    suspend fun getSchedulingPeriod(id: Int) = service.getSchedulingPeriod(id)

    suspend fun getUpcomingPeriod() = service.getUpcomingPeriod()

    suspend fun getSchedulingUnits(periodId: Int) = service.getSchedulingUnits(periodId)

    suspend fun addShiftTemplate(template: ShiftTemplate) = service.addShiftTemplate(template)

    suspend fun getShiftTemplates(unitId: Int) = service.getShiftTemplates(unitId)

    suspend fun getShiftTemplateEmployees(templateId: Int, page: Int = 1) =
        service.getTemplateEmployees(templateId, page)

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

    suspend fun callAutoScheduler(
        periodId: Int,
        iterations: Int = 2,
        noEmpty: Int = 80,
        demandFulfill: Int = 100,
        specializedPreferred: Int = 60,
        freeDays: Int = 30
    ) = service.callAutoScheduler(periodId, SchedulingParams(
        iterations = iterations,
        priorities = SchedulingPriorities(
            no_empty_shifts = noEmpty,
            free_days = freeDays,
            demand_fulfill = demandFulfill,
            specialized_preferred = specializedPreferred,
        )
    ))


    suspend fun submitSchedule(periodId: Int) = service.submitSchedule(periodId)
}
