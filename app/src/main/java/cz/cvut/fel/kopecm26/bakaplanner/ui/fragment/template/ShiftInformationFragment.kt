package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.template

import androidx.lifecycle.ViewModelStoreOwner
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentShiftTemplateInformationBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ShiftTemplateViewModel

class ShiftInformationFragment(
    private val viewModelStoreOwner: ViewModelStoreOwner? = null,
    val onSignUp: () -> Unit
) :
    ViewModelFragment<ShiftTemplateViewModel, FragmentShiftTemplateInformationBinding>(
        R.layout.fragment_shift_template_information,
        ShiftTemplateViewModel::class
    ) {
    override val viewModelOwner: ViewModelStoreOwner? get() = viewModelStoreOwner

    override fun initUi() {
        binding.btnAdd.setOnClickListener { onSignUp() }
    }
}
