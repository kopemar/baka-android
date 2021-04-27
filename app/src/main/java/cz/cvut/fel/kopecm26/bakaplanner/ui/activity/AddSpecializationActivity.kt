package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.afollestad.vvalidator.form
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityAddSpecializationBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.onClickOrFocus
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.finishWithOkResult
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hideKeyboard
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.showKeyboard
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.AddSpecializationViewModel

class AddSpecializationActivity :
    ViewModelActivity<AddSpecializationViewModel, ActivityAddSpecializationBinding>(
        R.layout.activity_add_specialization,
        AddSpecializationViewModel::class
    ) {

    override val toolbar: Toolbar get() = binding.mainToolbar.toolbar
    override val navigateUp: Boolean = true
    override val navigateUpRes = R.drawable.ic_mdi_close

    private var menu: Menu? = null

    private val workingObserver by lazy {
        Observer<Boolean> {
            if (it) hideKeyboard()
        }
    }

    private val successObserver by lazy {
        Observer<Boolean> {
            if (it) finishWithOkResult()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.check, menu)
        setupMenu()
        return super.onCreateOptionsMenu(menu)
    }

    override fun initUi() {
        showKeyboard(binding.actvName)

        binding.wrapper.onClickOrFocus {
            hideKeyboard()
        }

        toolbar.title = getString(R.string.add_specialization)

        viewModel.working.observe(this, workingObserver)
        viewModel.success.observe(this, successObserver)
    }

    private fun setupMenu() {
        form {
            inputLayout(R.id.input_name) {
                isNotEmpty()
            }

            menu?.let {
                submitWith(it, R.id.action_check) { viewModel.submit() }
            }
        }
    }

}