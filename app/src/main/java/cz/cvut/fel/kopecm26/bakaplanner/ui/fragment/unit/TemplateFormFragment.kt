package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.unit

import android.app.TimePickerDialog
import android.view.Menu
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelStoreOwner
import com.afollestad.vvalidator.form
import com.afollestad.vvalidator.form.Form
import com.afollestad.vvalidator.form.FormResult
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentTemplateFormBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListBreakBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListPriorityBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Break
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Priority
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.array.BaseArrayAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.getHour
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.getMinute
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hoursAndMinutes
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.isCzech
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.TemplateFormViewModel
import java.time.LocalTime

class TemplateFormFragment : ViewModelFragment<TemplateFormViewModel, FragmentTemplateFormBinding>(
    R.layout.fragment_template_form,
    TemplateFormViewModel::class
) {
    override val viewModelOwner: ViewModelStoreOwner?
        get() = activity

    private val prioritySelectedListener =
        AdapterView.OnItemClickListener { _, _, position, _ ->
            Logger.d("onItemSelected")
            viewModel.priority.value = Priority.values()[position]
        }

    private val breakSelectedListener =
        AdapterView.OnItemClickListener { _, _, position, _ ->
            Logger.d("onItemSelected")
            viewModel.breakMinutes.value = Break.breakMinutes[position]
        }

    override fun initUi() {
        binding.etStart.setUpTimePicker(viewModel.startTime)
        binding.etEnd.setUpTimePicker(viewModel.endTime)
        setupPrioritySelect()
        setupBreakSelect()
    }

    /**
     * Has to be called from fragment parent.
     */
    fun setupFormValidation(menu: Menu, menuItem: Int, onSubmit: (FormResult) -> Unit) {
        form {
            setUpFormFields()
            submitWith(menu, menuItem, onSubmit)
        }
    }

    private fun Form.setUpFormFields() = apply {
        input(binding.etStart.id) {
            assert(getString(R.string.field_must_be_filled)) {
                it.text.isNotEmpty()
            }
        }
        input(binding.etEnd.id) {
            assert(getString(R.string.field_must_be_filled)) {
                it.text.isNotEmpty()
            }
        }
        input(binding.actvBreakSpinner.id) {
            assert(getString(R.string.field_must_be_filled)) {
                viewModel.breakMinutes.value != null
            }
        }

        input(binding.actvDemandSpinner.id) {
            assert(getString(R.string.set_some_demand)) {
                viewModel.priority.value != null
            }
        }

    }

    private fun EditText.setUpTimePicker(
        observable: MutableLiveData<String>
    ) {
        setOnClickListener {
            TimePickerDialog(
                requireContext(),
                R.style.AlertDialogTheme,
                { _, hourOfDay, minute ->
                    Logger.d(hourOfDay)
                    val dateTime = LocalTime.of(hourOfDay, minute)
                    Logger.d(dateTime.toString())
                    observable.value = dateTime.toString()
                    this.setText(dateTime.hoursAndMinutes())
                },
                observable.value.toString().getHour() ?: 9,
                observable.value.toString().getMinute() ?: 0,
                isCzech()
            ).show()
        }
    }

    private fun setupPrioritySelect() {
        val adapter = BaseArrayAdapter<Priority, ListPriorityBinding>(
            requireContext(),
            R.layout.list_priority,
            { item, binding, _ -> binding.priority = item },
            Priority.values().toList()
        )

        binding.actvDemandSpinner.setupAdapter(adapter, prioritySelectedListener)
    }

    private fun setupBreakSelect() {
        val adapter = BaseArrayAdapter<Int, ListBreakBinding>(
            requireContext(),
            R.layout.list_break,
            { item, binding, _ -> binding.minutes = item },
            Break.breakMinutes
        )

        binding.actvBreakSpinner.setupAdapter(adapter, breakSelectedListener)
    }

    private fun AutoCompleteTextView.setupAdapter(
        adapter: BaseArrayAdapter<*, *>,
        listener: AdapterView.OnItemClickListener
    ) {
        setAdapter(adapter)
        onItemClickListener = listener
    }
}