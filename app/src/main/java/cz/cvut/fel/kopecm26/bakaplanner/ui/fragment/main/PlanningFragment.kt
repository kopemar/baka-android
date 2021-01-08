package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.main

import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentPlanningBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PlanningViewModel

class PlanningFragment: ViewModelFragment<PlanningViewModel, FragmentPlanningBinding>(R.layout.fragment_planning, PlanningViewModel::class) {
}