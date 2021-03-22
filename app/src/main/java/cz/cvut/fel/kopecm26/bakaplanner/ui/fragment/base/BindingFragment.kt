package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import cz.cvut.fel.kopecm26.bakaplanner.BR
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils

abstract class BindingFragment<B : ViewDataBinding>(private val layoutRes: Int) : Fragment() {

    protected lateinit var binding: B

    protected open val toolbar: Toolbar? = null
    protected open var navigateUp: Boolean = false

    @DrawableRes
    protected open val navigateUpRes = R.drawable.ic_mdi_back
    protected open val onNavigateUp: () -> Unit = { findNavController().navigateUp() }


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
                setNavigationIcon(navigateUpRes)
                setNavigationOnClickListener {
                    onNavigateUp.invoke()
                }
            }
        }
    }

    inline fun <reified T : AppCompatActivity> startActivityForResult(requestCode: Int) =
        Intent(requireContext(), T::class.java).apply {
            startActivityForResult(this, requestCode)
        }

    inline fun <reified T : AppCompatActivity> startActivityForResult(
        requestCode: Int,
        extras: Bundle.() -> Bundle
    ) = Intent(requireContext(), T::class.java).apply {
        this.putExtras(extras.invoke(Bundle()))
        startActivityForResult(this, requestCode)
    }

    protected open fun init() {}

    open fun initUi() {}

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)

        binding.lifecycleOwner = this

        binding.setVariable(BR.USER, PrefsUtils.getUser())
    }

    protected fun showSnackBar(text: String, length: Int = Snackbar.LENGTH_SHORT) =
        Snackbar.make(binding.root, text, length).apply { show() }

    protected fun showSnackBar(@StringRes text: Int, length: Int = Snackbar.LENGTH_SHORT) =
        showSnackBar(getString(text), length)

    protected fun showMaterialDialog(
        @StringRes message: Int,
        @StringRes title: Int? = null,
        @StringRes positive: Int? = R.string.ok,
        onPositive: ((DialogInterface) -> Unit?)? = null,
        @StringRes negative: Int? = R.string.cancel,
        onNegative: ((DialogInterface) -> Unit?)? = null,
    ): AlertDialog = MaterialAlertDialogBuilder(requireContext())
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
        }.show()
}
