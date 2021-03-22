package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.employees

import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentSelectEmployeesBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListEmployeeSelectableBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.FetchEmployeesStrategy
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.elevationOnScroll
import cz.cvut.fel.kopecm26.bakaplanner.util.Selection
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.SelectEmployeesViewModel

class SelectEmployeesFragment :
    ViewModelFragment<SelectEmployeesViewModel, FragmentSelectEmployeesBinding>(
        R.layout.fragment_select_employees,
        SelectEmployeesViewModel::class
    ) {
    override val toolbar: Toolbar get() = binding.selectToolbar.toolbar
    override var navigateUp: Boolean = true

    override val onNavigateUp: () -> Unit = {
        if ((viewModel.selectedCount.value ?: 0) > 0) {
            viewModel.employeeSelection.value?.unselectAll()
            viewModel.countSelected()
            binding.rvSelections.adapter?.notifyDataSetChanged()
        } else {
            findNavController().navigateUp()
        }
    }

    private val employeeAdapter by lazy {
        BaseListAdapter<Selection<Employee>>(
            { layoutInflater, viewGroup, attachToRoot ->
                ListEmployeeSelectableBinding.inflate(
                    layoutInflater,
                    viewGroup,
                    attachToRoot
                )
            },
            { item, binding, _ -> (binding as ListEmployeeSelectableBinding).employee = item },
            { item, _ -> selectEmployee(item) },
            { old, new -> old.item.id == new.item.id },
            { old, new -> old == new }
        )
    }

    private val countObserver by lazy {
        Observer<Int> {
            setupSelected()
        }
    }

    override fun initUi() {
        Logger.d("initUi")
        binding.selectToolbar.appBarLayout.elevationOnScroll(binding.rvSelections)

        viewModel.employeeSelection.observe(this) {
            binding.rvSelections.adapter = employeeAdapter.apply {
                viewModel.employeeSelection.value?.let(::setItems)
            }
        }

        viewModel.fetchEmployees(FetchEmployeesStrategy.General)

        viewModel.selectedCount.observe(viewLifecycleOwner, countObserver)
        
        setupMenu()
    }


    private fun setupMenu() {
        toolbar.inflateMenu(R.menu.check)

        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_check) {
//                startActivityForResult<AutoScheduleActivity>(PeriodFragment.AUTO_SCHEDULE_RC) {
//                    this.apply { putSerializable(AutoScheduleActivity.SCHEDULING_PERIOD, viewModel.period.value) }
//                }
            }
            true
        }
    }


    override fun onPause() {
        super.onPause()
        activity?.window?.statusBarColor = getColor(requireActivity(), R.color.bg_surface)
    }

    override fun onResume() {
        super.onResume()
        setupSelected()
    }

    private fun selectEmployee(day: Selection<Employee>) {
        with(viewModel.employeeSelection.value) {
            if (day.selected) {
                this?.unselect(day) { binding.rvSelections.adapter?.notifyItemChanged(it) }
            } else {
                this?.select(day) { binding.rvSelections.adapter?.notifyItemChanged(it) }
            }
            viewModel.countSelected()
        }
    }

    private fun setupSelected() {
        val selectedCount = viewModel.selectedCount.value ?: 0
        if (selectedCount > 0) {
            toolbar.setNavigationIcon(R.drawable.ic_mdi_close)
            activity?.window?.statusBarColor = getColor(requireActivity(), R.color.colorPrimary)
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_mdi_back)
            activity?.window?.statusBarColor = getColor(requireActivity(), R.color.bg_surface)
        }
    }
}
