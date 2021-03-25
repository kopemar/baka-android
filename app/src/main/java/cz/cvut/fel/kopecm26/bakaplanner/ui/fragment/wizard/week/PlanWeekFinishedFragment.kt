package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week

import androidx.navigation.fragment.findNavController
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentWeekFinishedBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.BindingFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.finishWithOkResult

class PlanWeekFinishedFragment :
    BindingFragment<FragmentWeekFinishedBinding>(R.layout.fragment_week_finished) {

    override fun initUi() {
        binding.btnFinish.setOnClickListener {
            activity?.finishWithOkResult()
        }

        binding.btnPlanShifts.setOnClickListener {
            findNavController().navigate(PlanWeekFinishedFragmentDirections.navigateToPlanDemand())
        }
    }
}
