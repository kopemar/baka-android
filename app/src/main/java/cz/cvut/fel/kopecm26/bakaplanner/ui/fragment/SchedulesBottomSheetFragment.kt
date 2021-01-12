package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import cz.cvut.fel.kopecm26.bakaplanner.BR
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.BaseViewModel
import kotlin.reflect.KClass

abstract class BaseBottomSheetDialogFragment<V: BaseViewModel, B: ViewDataBinding>(@LayoutRes val layoutRes: Int, clazz: KClass<V>): BottomSheetDialogFragment() {

    protected lateinit var binding: B
    protected open val viewModelOwner: ViewModelStoreOwner? = null

    open val viewModel by lazy { clazz.let { ViewModelProvider(viewModelOwner ?: this).get(it.java) } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBinding(inflater, container)
        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    protected open fun init() {}

    open fun initUi() {}

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)

        binding.lifecycleOwner = this

        binding.setVariable(BR.USER, PrefsUtils.getUser())
    }


}