package cz.cvut.fel.kopecm26.bakaplanner.di

import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.SignUpToShiftViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.TemplateFormViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val sharedViewModelModule = module {
    viewModel { SignUpToShiftViewModel() }
    viewModel { TemplateFormViewModel() }
}