package cz.cvut.fel.kopecm26.bakaplanner.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar

abstract class BindingActivity<B : ViewDataBinding>(private val layoutRes: Int) : AppCompatActivity() {

    protected lateinit var binding: B

    /**
     * Set value to true if status bar of given child activity should be transparent
     */
    protected open val statusBarTransparent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        init()
        initUi()

        initStatusBar()
    }

    protected open fun init() {}

    open fun initUi() {}

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, layoutRes)

        binding.lifecycleOwner = this
    }

    private fun initStatusBar() {
        if (statusBarTransparent) {
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    protected fun showSnackBar(text: String, length: Int = Snackbar.LENGTH_SHORT) = Snackbar.make(binding.root, text, length).show()
    protected fun showSnackBar(text: Int, length: Int = Snackbar.LENGTH_SHORT) = showSnackBar(getString(text), length)
}