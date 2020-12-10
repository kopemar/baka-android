package cz.cvut.fel.kopecm26.bakaplanner.di

import android.content.Context
import cz.cvut.fel.kopecm26.bakaplanner.datasource.PlannerDatabase
import cz.cvut.fel.kopecm26.bakaplanner.repository.ShiftRepository
import cz.cvut.fel.kopecm26.bakaplanner.repository.UserRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

val databaseModule: Module = module {
    fun buildDatabase(context: Context): PlannerDatabase = PlannerDatabase.buildDatabase(context)

    fun getShiftDao(database: PlannerDatabase) = database.getShiftDao()

    single { buildDatabase(androidApplication()) }
    single { getShiftDao(get()) }
}

val repositoryModule = module {
    single { UserRepository(get()) }
    single { ShiftRepository(get(), get()) }
}