package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityAddSpecializationBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.onClickOrFocus
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.check, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun initUi() {
        showKeyboard(binding.actvName)

        binding.wrapper.onClickOrFocus {
            hideKeyboard()
        }

        viewModel.working.observe(this, workingObserver)
    }

}