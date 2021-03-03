package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import cz.cvut.fel.kopecm26.bakaplanner.BR
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.BaseViewModel
import kotlin.reflect.KClass

abstract class ViewModelBottomSheetFragment<V : BaseViewModel, B : ViewDataBinding>(@LayoutRes layoutRes: Int, clazz: KClass<V>) : BaseBottomSheetDialogFragment<B>(layoutRes) {

    open val viewModelOwner: ViewModelStoreOwner? = null

    open val viewModel by lazy { clazz.let { ViewModelProvider(viewModelOwner ?: this).get(it.java) } }

    override fun init() {
        binding.setVariable(BR.vm, viewModel)
    }
}
