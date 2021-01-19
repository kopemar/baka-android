package cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base

import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import cz.cvut.fel.kopecm26.bakaplanner.R

abstract class BindingActivity<B : ViewDataBinding>(@LayoutRes private val layoutRes: Int) : AppCompatActivity() {

    protected lateinit var binding: B

    /**
     * Set value to true if status bar of given child activity should be transparent
     */
    protected open val statusBarTransparent = false

    protected open val toolbar: Toolbar? = null
    protected open val navigateUp: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initNavigation()
        init()
        initUi()

        initStatusBar()
        initToolbar()
    }

    protected open fun initNavigation() {}

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

    private fun initToolbar() {
        toolbar?.let {
            this.setSupportActionBar(it)
            supportActionBar?.setDisplayHomeAsUpEnabled(navigateUp)
        }
    }

    protected fun showSnackBar(text: String, length: Int = Snackbar.LENGTH_SHORT) =
        Snackbar.make(binding.root, text, length).show()

    protected fun showSnackBar(text: Int, length: Int = Snackbar.LENGTH_SHORT) =
        showSnackBar(getString(text), length)

    protected fun showMaterialDialog(
        @StringRes message: Int,
        @StringRes title: Int? = null,
        @StringRes positive: Int? = R.string.ok,
        onPositive: ((DialogInterface) -> Unit?)? = null,
        @StringRes negative: Int? = R.string.cancel,
        onNegative: ((DialogInterface) -> Unit?)? = null,
    ) {
        MaterialAlertDialogBuilder(this)
            .setMessage(message)
            .apply {
                title?.let(::setTitle)
                positive?.let {
                    this.setPositiveButton(getString(it)) { dialog, _ ->
                        onPositive?.invoke(
                            dialog
                        )
                    }
                }
                negative?.let {
                    this.setNegativeButton(getString(it)) { dialog, _ ->
                        onNegative?.invoke(
                            dialog
                        )
                    }
                }
            }
            .show()
    }
}