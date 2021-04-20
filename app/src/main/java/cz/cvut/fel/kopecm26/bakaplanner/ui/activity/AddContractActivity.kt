package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import android.view.Menu
import android.widget.AdapterView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.afollestad.vvalidator.form
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityAddContractBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListIntBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ContractTypes
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.CreateContractResponse
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.array.BaseArrayAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.forms.values.Workload
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.onClickOrFocus
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.setupAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.showDatePicker
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.finishWithOkResult
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.AddContractViewModel
import java.time.LocalDate

class AddContractActivity : ViewModelActivity<AddContractViewModel, ActivityAddContractBinding>(
    R.layout.activity_add_contract,
    AddContractViewModel::class
) {
    override val toolbar: Toolbar get() = binding.addContractToolbar.toolbar
    override val navigateUp = true
    override val onNavigateUp: (BackNavigation) -> Unit = {
        showMaterialDialog(
            message = R.string.are_you_sure_you_want_to_cancel,
            positive = R.string.yes,
            onPositive = {
                finish()
            },
            negative = R.string.no
        )
    }

    private val employeeId by lazy { intent?.extras?.getInt(EMPLOYEE_EXTRA_KEY) }

    private val contractType by lazy {
        AdapterView.OnItemClickListener { _, _, position, _ ->
            viewModel.setContractType(ContractTypes.all[position])
        }
    }

    private val workLoad by lazy {
        AdapterView.OnItemClickListener { _, _, position, _ ->
            viewModel.setWorkLoad(Workload.all[position])
        }
    }

    private val responseObserver by lazy {
        Observer<CreateContractResponse> {
            if (it.success) {
                finishWithOkResult()
            }
        }
    }

    override fun initUi() {
        employeeId?.let { viewModel.setEmployee(it) }
        viewModel.response.observe(this, responseObserver)

        setupTypeAdapter()
        setupWorkloadAdapter()

        setupDatePickers()
    }

    private fun setupForm(menu: Menu) {
        form {
            inputLayout(binding.inputStartDate) {
                isNotEmpty()
            }

            inputLayout(binding.inputEndDate) {
                this.assert(getString(R.string.end_date)) {
                    viewModel.indefinite.value == true || viewModel.endDate.value != null
                }
            }
            submitWith(menu, R.id.action_check) {
                viewModel.submit()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.check, menu)
        setupMenu(menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupMenu(menu: Menu?) {
        menu?.let { setupForm(it) }
    }

    private fun setupWorkloadAdapter() {
        val adapter = BaseArrayAdapter<Float, ListIntBinding>(
            this,
            R.layout.list_int,
            { item, binding, _ -> binding.number = item.toString() },
            Workload.all
        )

        binding.actvWorkLoad.setupAdapter(
            adapter,
            workLoad
        )
    }

    private fun setupTypeAdapter() {
        val adapter = BaseArrayAdapter<ContractTypes, ListIntBinding>(
            this,
            R.layout.list_int,
            { item, binding, _ -> binding.number = getString(item.nameRes) },
            ContractTypes.all
        )

        binding.actvContractType.setupAdapter(
            adapter,
            contractType
        )
    }

    private fun setupDatePickers() {
        val today = LocalDate.now()
        val start = viewModel.startDate.value
        val end = viewModel.endDate.value
        binding.actvStartDate.onClickOrFocus {
            this@AddContractActivity.showDatePicker(
                year = start?.year ?: today.year,
                month = start?.monthValue ?: today.monthValue - 1,
                day = start?.dayOfMonth ?: today.dayOfMonth,
                maxYearsAgo = null
            ) {
                viewModel.setStartDate(it)
            }
        }

        binding.actvEndDate.onClickOrFocus {
            this@AddContractActivity.showDatePicker(
                year = end?.year ?: today.year + 1,
                month = end?.monthValue ?: today.monthValue - 1,
                day = end?.dayOfMonth ?: today.dayOfMonth,
                maxYearsAgo = null
            ) {
                viewModel.setEndDate(it)
            }
        }
    }

    companion object {
        const val EMPLOYEE_EXTRA_KEY = "EMPLOYEE_EXTRA"
    }
}
