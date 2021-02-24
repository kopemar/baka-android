package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base

import android.app.TimePickerDialog
import android.widget.EditText
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import cz.cvut.fel.kopecm26.bakaplanner.BR
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.getHour
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.getMinute
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hoursAndMinutes
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.isCzech
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.BaseViewModel
import java.time.LocalTime
import kotlin.reflect.KClass

abstract class ViewModelFragment<V : BaseViewModel, B : ViewDataBinding>(layoutRes: Int, private val clazz: KClass<V>) : BindingFragment<B>(layoutRes) {

    protected open val viewModelOwner: ViewModelStoreOwner? = null

    open val viewModel by lazy { clazz.let { ViewModelProvider(viewModelOwner ?: this).get(it.java) } }

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

    protected fun EditText.setUpTimePicker(
        observable: MutableLiveData<String>
    ) {
        TimePickerDialog(
            requireContext(),
            R.style.AlertDialogTheme,
            { _, hourOfDay, minute ->
                val dateTime = LocalTime.of(hourOfDay, minute)
                observable.value = dateTime.toString()
                this.setText(dateTime.hoursAndMinutes())
            },
            observable.value.toString().getHour() ?: 9,
            observable.value.toString().getMinute() ?: 0,
            isCzech()
        ).show()
    }
}
