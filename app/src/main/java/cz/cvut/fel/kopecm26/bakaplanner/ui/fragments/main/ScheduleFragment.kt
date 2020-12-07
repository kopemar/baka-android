package cz.cvut.fel.kopecm26.bakaplanner.ui.fragments.main

import androidx.lifecycle.viewModelScope
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentScheduleBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragments.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ScheduleViewModel
import kotlinx.coroutines.launch

class ScheduleFragment : ViewModelFragment<ScheduleViewModel, FragmentScheduleBinding>(R.layout.fragment_schedule,ScheduleViewModel::class) {

    override fun initUi() {
        binding.btnTest.setOnClickListener {
            viewModel.viewModelScope.launch {
                viewModel.getShifts()
            }
        }
    }
}