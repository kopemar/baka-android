package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.EditText
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import cz.cvut.fel.kopecm26.bakaplanner.BR
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.DateTimeFormats
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.formatTime
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.getHour
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.getMinute
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.isCzech
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.BaseViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import kotlin.reflect.KClass

abstract class ViewModelFragment<V : BaseViewModel, B : ViewDataBinding>(layoutRes: Int, private val clazz: KClass<V>) : BindingFragment<B>(layoutRes) {

    protected open val viewModelOwner: ViewModelStoreOwner? = null

    open val viewModel by lazy { clazz.let { ViewModelProvider(viewModelOwner ?: this).get(it.java) } }

    // TODO refactor code
    val sharedViewModel: SharedViewModel by sharedViewModel()

    protected open fun errorObserver() = Observer<Int?> {
        it?.let {
            showSnackBar(it)
            viewModel.removeError()
        }
    }

    override fun init() {
        binding.setVariable(BR.vm, viewModel)

        viewModel.errorMessage.observe(this, errorObserver())
    }

    @Deprecated("Will be removed in release")
    protected fun EditText.setUpTimePicker(
        observable: MutableLiveData<String>
    ) {
        TimePickerDialog(
            requireContext(),
            R.style.AlertDialogTheme,
            { _, hourOfDay, minute ->
                val dateTime = LocalTime.of(hourOfDay, minute)
                observable.value = dateTime.toString()
                this.setText(dateTime.formatTime(DateTimeFormats.HOURS_MINUTES))
            },
            observable.value.toString().getHour() ?: 9,
            observable.value.toString().getMinute() ?: 0,
            isCzech()
        ).show()
    }

    protected fun setUpTimePicker(
        hour: Int? = null,
        minute: Int? = null,
        onPicked: (LocalTime) -> Unit
    ) {
        TimePickerDialog(
            requireContext(),
            R.style.AlertDialogTheme,
            { _, hourOfDay, min ->
                onPicked.invoke(LocalTime.of(hourOfDay, min))
            },
            hour ?: 9,
            minute ?: 0,
            isCzech()
        ).show()
    }

    protected fun setUpDatePicker(
        year: Int? = null,
        month: Int? = null,
        day: Int? = null,
        showYearFirst: Boolean = true,
        maxYearsAgo: Long = 15,
        onPicked: (LocalDate) -> Unit
    ) {
        val defaultDate = LocalDate.of(1990, 1, 1)
        DatePickerDialog(
            requireContext(),
            R.style.AlertDialogTheme,
            { _, y, m, d ->
                val dateTime = LocalDate.of(y, m + 1, d)
                onPicked(dateTime)
            },
            year ?: defaultDate.year,
            month ?: defaultDate.monthValue - 1,
            day ?: defaultDate.dayOfMonth,
        ).apply {
            // This is StackOverflow workaround to display year picker first…
            if (showYearFirst) this.datePicker.touchables.firstOrNull()?.performClick()
            this.datePicker.maxDate = LocalDateTime.now().minusYears(maxYearsAgo).toEpochSecond(ZoneOffset.UTC) * 1000
        }.show()
    }
}
