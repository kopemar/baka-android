package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.organization

import android.app.Activity
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentOrganizationEmployeesBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListEmployeeBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.AddEmployeeActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.PagingAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.elevationOnScroll
import cz.cvut.fel.kopecm26.bakaplanner.util.binding.onRefresh
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.OrganizationEmployeesViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OrganizationEmployeesFragment :
    ViewModelFragment<OrganizationEmployeesViewModel, FragmentOrganizationEmployeesBinding>(
        R.layout.fragment_organization_employees,
        OrganizationEmployeesViewModel::class
    ) {

    override val toolbar: Toolbar get() = binding.mainToolbar.toolbar
    override var navigateUp = true
    override val viewModelOwner: ViewModelStoreOwner? get() = activity

    private var job: Job? = null

    private val employeesAdapter by lazy {
        PagingAdapter<Employee>(
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
            { employee, _ ->
                findNavController().navigate(
                    OrganizationEmployeesFragmentDirections.navigateToEmployeeDetail(
                        employee
                    )
                )
            },
            { old, new -> old.id == new.id },
            { old, new -> old == new }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_EMPLOYEE_REQUEST && resultCode == Activity.RESULT_OK) {
            employeesAdapter.refresh()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun initUi() {
//        viewModel.fetchOrganizationEmployees()

        binding.fab.setOnClickListener {
            startActivityForResult<AddEmployeeActivity>(ADD_EMPLOYEE_REQUEST)
        }

        binding.mainToolbar.appBarLayout.elevationOnScroll(binding.rvEmployees)


        binding.rvEmployees.apply {
            adapter = employeesAdapter
        }

        binding.swipeRefresh.onRefresh {
            employeesAdapter.refresh()
        }

        collectLatest()
    }

    private fun collectLatest() {
        job?.cancel()

        job = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flow.collectLatest { employeesAdapter.submitData(it) }
        }
    }

    companion object {
        const val ADD_EMPLOYEE_REQUEST = 1111
    }
}
