package cz.cvut.fel.kopecm26.bakaplanner.di

import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PeriodDaysViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PlanDaysViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.SharedViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.TemplateFormViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val sharedViewModelModule = module {
    viewModel { SharedViewModel() }
    viewModel { TemplateFormViewModel() }

    viewModel { PeriodDaysViewModel() }
    viewModel { PlanDaysViewModel() }
}