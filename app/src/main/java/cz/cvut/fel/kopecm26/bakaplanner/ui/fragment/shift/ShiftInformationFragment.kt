package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.shift

import androidx.lifecycle.ViewModelStoreOwner
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentShiftInformationBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ShiftViewModel
import kotlin.reflect.KClass

class ShiftInformationFragment(
    clazz: KClass<ShiftViewModel>,
    private val viewModelStoreOwner: ViewModelStoreOwner? = null,
    private val onRemoveShift: () -> Unit,
) :
    ViewModelFragment<ShiftViewModel, FragmentShiftInformationBinding>(
        R.layout.fragment_shift_information,
        clazz
    ) {
    override val viewModelOwner: ViewModelStoreOwner? get() = viewModelStoreOwner ?: activity

    override fun initUi() {
        binding.btnRemove.setOnClickListener { onRemoveShift() }
    }
}
