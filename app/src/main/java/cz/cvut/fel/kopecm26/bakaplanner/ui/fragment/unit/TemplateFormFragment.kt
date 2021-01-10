package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.unit

import android.app.TimePickerDialog
import android.view.Menu
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelStoreOwner
import com.afollestad.vvalidator.form
import com.afollestad.vvalidator.form.Form
import com.afollestad.vvalidator.form.FormResult
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentTemplateFormBinding
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

    override fun initUi() {
        binding.etStart.setUpTimePicker(viewModel.startTime)
        binding.etEnd.setUpTimePicker(viewModel.endTime)
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
        input(binding.etBreakMinutes.id) {
            assert(getString(R.string.field_must_be_filled)) {
                it.text.toString().toIntOrNull()?.run{ this >= 0} ?: false
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
}