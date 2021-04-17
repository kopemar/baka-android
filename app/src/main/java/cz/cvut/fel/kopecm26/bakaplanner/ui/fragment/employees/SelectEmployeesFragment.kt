package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.employees

import android.annotation.SuppressLint
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentSelectEmployeesBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListEmployeeSelectableBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.EmployeePresenter
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.elevationOnScroll
import cz.cvut.fel.kopecm26.bakaplanner.util.Consumable
import cz.cvut.fel.kopecm26.bakaplanner.util.Selection
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.SelectEmployeesViewModel

class SelectEmployeesFragment :
    ViewModelFragment<SelectEmployeesViewModel, FragmentSelectEmployeesBinding>(
        R.layout.fragment_select_employees,
        SelectEmployeesViewModel::class
    ) {
    override val toolbar: Toolbar get() = binding.selectToolbar.toolbar
    override var navigateUp: Boolean = true

    @SuppressLint("NotifyDataSetChanged")
    override val onNavigateUp: () -> Unit = {
        if ((viewModel.selectedCount.value ?: 0) > 0) {
            viewModel.employeeSelection.value?.unselectAll()
            viewModel.countSelected()
            binding.rvSelections.adapter?.notifyDataSetChanged()
        } else {
            findNavController().navigateUp()
        }
    }

    private val args by navArgs<SelectEmployeesFragmentArgs>()

    private val employeeAdapter by lazy {
        BaseListAdapter<Selection<EmployeePresenter>>(
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

    private val successObserver by lazy {
        Observer<Boolean> {
            if (it) {
                sharedViewModel.assignSuccess.value = Consumable(true)
                findNavController().navigateUp()
            }
        }
    }

    override fun initUi() {
        viewModel.fetchEmployees(args.type)
        binding.selectToolbar.appBarLayout.elevationOnScroll(binding.rvSelections)

        viewModel.success.observe(this, successObserver)

        viewModel.employeeSelection.observe(this) {
            Logger.d("Got employee selection")
            binding.rvSelections.adapter = employeeAdapter.apply {
                viewModel.employeeSelection.value?.let(::setItems)
            }
        }

        viewModel.selectedCount.observe(viewLifecycleOwner, countObserver)

        setupMenu()
    }


    private fun setupMenu() {
        toolbar.inflateMenu(R.menu.check)

        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_check) {
                viewModel.submit(args.type)
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

    private fun selectEmployee(employee: Selection<EmployeePresenter>) {
        with(viewModel.employeeSelection.value) {
            if (employee.selected) {
                this?.unselect(employee) { binding.rvSelections.adapter?.notifyItemChanged(it) }
            } else {
                this?.select(employee) { binding.rvSelections.adapter?.notifyItemChanged(it) }
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
