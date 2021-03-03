package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.organization

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentOrganizationEmployeesBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListEmployeeBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.elevationOnScroll
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.OrganizationEmployeesViewModel

class OrganizationEmployeesFragment :
    ViewModelFragment<OrganizationEmployeesViewModel, FragmentOrganizationEmployeesBinding>(
        R.layout.fragment_organization_employees,
        OrganizationEmployeesViewModel::class
    ) {

    override val toolbar: Toolbar get() = binding.mainToolbar.toolbar
    override var navigateUp = true

    private val observer by lazy {
        Observer<List<Employee>> {
            binding.rvEmployees.adapter = BaseListAdapter<Employee>(
                { layoutInflater, viewGroup, attachToRoot ->
                    ListEmployeeBinding.inflate(
                        layoutInflater,
                        viewGroup,
                        attachToRoot
                    )
                },
                { employee, binding, _ ->
                    (binding as ListEmployeeBinding).employee = employee
                },
                null,
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
        }
    }

    override fun initUi() {
        PrefsUtils.getUser()?.organization_id?.let {
            viewModel.fetchOrganizationEmployees(it)
        }

        binding.mainToolbar.appBarLayout.elevationOnScroll(binding.rvEmployees)

        viewModel.employees.observe(viewLifecycleOwner, observer)
    }
}
