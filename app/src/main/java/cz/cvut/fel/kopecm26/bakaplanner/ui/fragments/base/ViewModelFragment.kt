package cz.cvut.fel.kopecm26.bakaplanner.ui.fragments.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import cz.cvut.fel.kopecm26.bakaplanner.BR
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.BaseViewModel
import kotlin.reflect.KClass

abstract class ViewModelFragment<V: BaseViewModel, B: ViewDataBinding>(layoutRes: Int, private val clazz: KClass<V>, private val viewModelOwner: ViewModelStoreOwner? = null): BindingFragment<B>(layoutRes) {

    open val viewModel by lazy { clazz.let { ViewModelProvider(viewModelOwner ?: this).get(it.java) } }

    protected open fun errorObserver() =  Observer<Int> { showSnackBar(it) }

    override fun init() {
        binding.setVariable(BR.vm, viewModel)

        viewModel.errorMessage.observe(this, errorObserver())
    }

}