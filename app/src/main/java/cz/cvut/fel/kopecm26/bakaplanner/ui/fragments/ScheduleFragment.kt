package cz.cvut.fel.kopecm26.bakaplanner.ui.fragments

import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentScheduleBinding
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ScheduleViewModel

class ScheduleFragment : ViewModelFragment<ScheduleViewModel, FragmentScheduleBinding>(R.layout.fragment_schedule,ScheduleViewModel::class)