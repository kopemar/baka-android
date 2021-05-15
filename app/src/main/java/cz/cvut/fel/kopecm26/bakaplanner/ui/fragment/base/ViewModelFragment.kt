package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base

import android.app.TimePickerDialog
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import cz.cvut.fel.kopecm26.bakaplanner.BR
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.showDatePicker
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.isCzech
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.BaseViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.time.LocalDate
import java.time.LocalTime
import kotlin.reflect.KClass

abstract class ViewModelFragment<V : BaseViewModel, B : ViewDataBinding>(layoutRes: Int, private val clazz: KClass<V>) : BindingFragment<B>(layoutRes) {

    protected open val viewModelOwner: ViewModelStoreOwner? = null

    open val viewModel by lazy { clazz.let { ViewModelProvider(viewModelOwner ?: this).get(it.java) } }

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

    @Deprecated("Implemented as context extension", replaceWith = ReplaceWith("Context.showDatePicker"))
    protected fun setUpDatePicker(
        year: Int? = null,
        month: Int? = null,
        day: Int? = null,
        showYearFirst: Boolean = true,
        maxYearsAgo: Long = 15,
        onPicked: (LocalDate) -> Unit
    ) {
        requireContext().showDatePicker(year, month, day, showYearFirst, maxYearsAgo, onPicked)
    }
}
