package cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import cz.cvut.fel.kopecm26.bakaplanner.BR
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.BaseViewModel
import kotlin.reflect.KClass

abstract class ViewModelActivity<V : BaseViewModel, B : ViewDataBinding>(
    @LayoutRes layoutRes: Int,
    clazz: KClass<V>
) : BindingActivity<B>(layoutRes) {

    // todo by viewModels
    open val viewModel by lazy { clazz.let { ViewModelProvider(this).get(it.java) } }

    override fun init() {
        val vm = viewModel
        binding.setVariable(BR.vm, vm)
    }
}
