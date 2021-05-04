package cz.cvut.fel.kopecm26.bakaplanner.di

import android.content.Context
import cz.cvut.fel.kopecm26.bakaplanner.db.PlannerDatabase
import cz.cvut.fel.kopecm26.bakaplanner.repository.ContractRepository
import cz.cvut.fel.kopecm26.bakaplanner.repository.PlanningRepository
import cz.cvut.fel.kopecm26.bakaplanner.repository.ScheduleRepository
import cz.cvut.fel.kopecm26.bakaplanner.repository.ShiftRepository
import cz.cvut.fel.kopecm26.bakaplanner.repository.SpecializationRepository
import cz.cvut.fel.kopecm26.bakaplanner.repository.UserRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

val databaseModule: Module = module {
    fun buildDatabase(context: Context): PlannerDatabase = PlannerDatabase.buildDatabase(context)

    fun getShiftDao(database: PlannerDatabase) = database.getShiftDao()

    fun getPeriodDao(database: PlannerDatabase) = database.getPeriodDao()

    single { buildDatabase(androidApplication()) }
    single { getShiftDao(get()) }
    single { getPeriodDao(get()) }
}

val repositoryModule = module {
    single { UserRepository(get()) }
    single { ScheduleRepository(get(), get()) }
    single { ShiftRepository(get(), get(), get()) }
    single { ContractRepository(get()) }
    single { PlanningRepository(get()) }
    single { SpecializationRepository(get()) }
}
