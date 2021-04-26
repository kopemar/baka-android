package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityAddEmployeeBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.employees.AddEmployeeFormFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.finishWithOkResult
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.getFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hideKeyboard
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.AddEmployeeViewModel

class AddEmployeeActivity : ViewModelActivity<AddEmployeeViewModel, ActivityAddEmployeeBinding>(
    R.layout.activity_add_employee,
    AddEmployeeViewModel::class
) {
    override val toolbar: Toolbar get() = binding.mainToolbar.toolbar
    override val navigateUp = true

    override val onNavigateUp: (BackNavigation) -> Unit = {
        if (viewModel.working.value == false) finish()
    }

    private val errorObserver by lazy {
        Observer<Int?> {
            it?.let {
                showSnackBar(it)
                viewModel.removeError()
            }
        }
    }

    override fun initUi() {
        toolbar.title = getString(R.string.add_employee)
        viewModel.working.observe(this) {
            if (it) hideKeyboard()
        }

        viewModel.response.observe(this) {
            if (it.success) finishWithOkResult()
        }

        viewModel.errorMessage.observe(this, errorObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.check, menu)
        setupMenu(menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_check) viewModel.submit()
        return super.onOptionsItemSelected(item)
    }

    private fun setupMenu(menu: Menu?) {
        menu?.let { m ->
            val templates = getFragment<AddEmployeeFormFragment>(R.id.fragment)

            templates?.setupFormValidation(m, R.id.action_check) {
                viewModel.submit()
            }
        }
        Logger.d("setupMenu")
    }
}
