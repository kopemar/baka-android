package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.unit

import android.app.TimePickerDialog
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelStoreOwner
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentTemplateFormBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.getHour
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.getMinute
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hoursAndMinutes
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.TemplateFormViewModel
import java.time.LocalTime

class TemplateFormFragment : ViewModelFragment<TemplateFormViewModel, FragmentTemplateFormBinding>(R.layout.fragment_template_form, TemplateFormViewModel::class) {
    override val viewModelOwner: ViewModelStoreOwner?
        get() = activity

    override fun initUi() {
        binding.etStart.setUpTimePicker(viewModel.startTime)
        binding.etEnd.setUpTimePicker(viewModel.endTime)
    }

    private fun EditText.setUpTimePicker(observable: MutableLiveData<String>) {
        setOnClickListener {
            TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    val dateTime = LocalTime.of(hourOfDay, minute)
                    observable.value = dateTime.toString()
                    this.setText(dateTime.hoursAndMinutes())
                },
                this.text.toString().getHour() ?: 9,
                this.text.toString().getMinute() ?: 0,
                false // todo 24h fmt
            ).show()
        }
    }
}