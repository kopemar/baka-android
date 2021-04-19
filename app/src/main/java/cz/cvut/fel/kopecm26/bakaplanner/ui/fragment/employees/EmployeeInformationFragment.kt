package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.employees

import androidx.lifecycle.ViewModelStoreOwner
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentEmployeeInformationBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.EmployeeViewModel

class EmployeeInformationFragment(
    val onSpecialization: () -> Unit
) :
    ViewModelFragment<EmployeeViewModel, FragmentEmployeeInformationBinding>(
        R.layout.fragment_employee_information,
        EmployeeViewModel::class
    ) {
    override val viewModelOwner: ViewModelStoreOwner? get() = activity

    override fun initUi() {
        binding.specializations.root.setOnClickListener {
            onSpecialization()
        }
    }
}
