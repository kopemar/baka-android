package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import cz.cvut.fel.kopecm26.bakaplanner.BR
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils

abstract class BindingFragment<B : ViewDataBinding>(private val layoutRes: Int) : Fragment() {

    protected lateinit var binding: B

    protected open val toolbar: Toolbar? = null
    protected open var navigateUp: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBinding(inflater, container)
        init()
        initToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initToolbar() {
        toolbar?.run {
            if (navigateUp) {
                setNavigationIcon(R.drawable.ic_mdi_back)
                setNavigationOnClickListener {
                    findNavController().navigateUp()
                }
            }
        }
    }

    protected open fun init() {}

    open fun initUi() {}

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)

        binding.lifecycleOwner = this

        binding.setVariable(BR.USER, PrefsUtils.getUser())
    }

    protected fun showSnackBar(text: String, length: Int = Snackbar.LENGTH_SHORT) =
        Snackbar.make(binding.root, text, length).show()

    protected fun showSnackBar(@StringRes text: Int, length: Int = Snackbar.LENGTH_SHORT) =
        showSnackBar(getString(text), length)
}