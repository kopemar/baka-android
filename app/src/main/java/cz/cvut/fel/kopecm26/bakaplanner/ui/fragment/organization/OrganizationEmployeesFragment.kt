package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.organization

import android.app.Activity
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentOrganizationEmployeesBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListEmployeeBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.AddEmployeeActivity
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
    override val viewModelOwner: ViewModelStoreOwner? get() = activity
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
                { employee, _ -> findNavController().navigate(OrganizationEmployeesFragmentDirections.navigateToEmployeeDetail(employee)) },
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_EMPLOYEE_REQUEST && resultCode == Activity.RESULT_OK) {
            fetchEmployees()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun initUi() {
        // todo better way :P
        fetchEmployees()

        binding.fab.setOnClickListener {
            startActivityForResult<AddEmployeeActivity>(ADD_EMPLOYEE_REQUEST)
        }

        binding.mainToolbar.appBarLayout.elevationOnScroll(binding.rvEmployees)

        viewModel.employees.observe(viewLifecycleOwner, observer)
    }

    fun fetchEmployees() = PrefsUtils.getUser()?.organization_id?.let {
        viewModel.fetchOrganizationEmployees(it)
    }

    companion object {
        const val ADD_EMPLOYEE_REQUEST = 1111
    }
}
