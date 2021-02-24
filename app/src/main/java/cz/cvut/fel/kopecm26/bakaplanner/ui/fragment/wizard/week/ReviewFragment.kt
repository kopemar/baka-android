package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week

import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentReviewBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PeriodDaysViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PlanDaysViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ReviewFragment : ViewModelFragment<PlanDaysViewModel, FragmentReviewBinding>(
    R.layout.fragment_review,
    PlanDaysViewModel::class
) {
    override val viewModelOwner get() = activity

    private val periodDaysViewModel: PeriodDaysViewModel by sharedViewModel()

    override fun initUi() {
        binding.pvm = periodDaysViewModel
    }
}
