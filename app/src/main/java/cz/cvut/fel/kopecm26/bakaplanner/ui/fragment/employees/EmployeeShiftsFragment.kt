package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.employees

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentEmployeeShiftsBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListShiftBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.EmployeeViewModel

class EmployeeShiftsFragment :
    ViewModelFragment<EmployeeViewModel, FragmentEmployeeShiftsBinding>(
        R.layout.fragment_employee_shifts,
        EmployeeViewModel::class
    ) {
    override val viewModelOwner: ViewModelStoreOwner? get() = activity

    var itemPressListener: ((shift: Shift) -> Unit)? = null

    private val observer by lazy {
        Observer<List<Shift>> {
            binding.shifts.layoutManager = LinearLayoutManager(binding.root.context)
            binding.shifts.adapter = BaseListAdapter<Shift>(
                { layoutInflater, viewGroup, attachToRoot ->
                    ListShiftBinding.inflate(
                        layoutInflater,
                        viewGroup,
                        attachToRoot
                    )
                },
                { shift, binding, _ -> (binding as ListShiftBinding).shift = shift },
                null,
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
        }
    }


    override fun initUi() {
        viewModel.shifts.observe(viewLifecycleOwner, observer)
    }
}
