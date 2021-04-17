package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityAddEmployeeBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.employees.AddEmployeeFormFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hideKeyboard
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.AddEmployeeViewModel

class AddEmployeeActivity : ViewModelActivity<AddEmployeeViewModel, ActivityAddEmployeeBinding>(
    R.layout.activity_add_employee,
    AddEmployeeViewModel::class
) {
    override val toolbar: Toolbar get() = binding.mainToolbar.toolbar
    override val navigateUp = true

    override val onNavigateUp = { if (viewModel.working.value == false) finish() }

    override fun initUi() {
        toolbar.title = getString(R.string.add_employee)
        viewModel.working.observe(this) {
            if (it) hideKeyboard()
        }

        viewModel.response.observe(this) {
            if (it.success) finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.check, menu)
        setupMenu(menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_check) viewModel.submit()
        return super.onOptionsItemSelected(item)
    }

    private fun setupMenu(menu: Menu?) {
        menu?.let { m ->
            val templates =
                supportFragmentManager.findFragmentById(R.id.fragment)?.childFragmentManager
                    ?.fragments?.firstOrNull { it is AddEmployeeFormFragment } as? AddEmployeeFormFragment
            templates?.setupFormValidation(m, R.id.menu_check) {
                viewModel.submit()
            }
        }
        Logger.d("setupMenu")
    }

}
