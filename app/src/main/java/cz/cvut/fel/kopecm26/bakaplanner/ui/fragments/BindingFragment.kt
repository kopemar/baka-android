package cz.cvut.fel.kopecm26.bakaplanner.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

abstract class BindingFragment<B: ViewDataBinding>(private val layoutRes: Int): Fragment() {

    protected lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBinding(inflater, container)
        init()
        initUi()
        return binding.root
    }

    protected open fun init() {}

    open fun initUi() {}

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)

        binding.lifecycleOwner = this
    }

    protected fun showSnackBar(text: String, length: Int = Snackbar.LENGTH_SHORT) = Snackbar.make(binding.root, text, length).show()
    protected fun showSnackBar(@StringRes text: Int, length: Int = Snackbar.LENGTH_SHORT) = showSnackBar(getString(text), length)
}