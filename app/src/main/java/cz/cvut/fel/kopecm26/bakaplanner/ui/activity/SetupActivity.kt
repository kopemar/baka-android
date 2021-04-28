package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import androidx.navigation.findNavController
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivitySetupBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.BindingActivity

class SetupActivity : BindingActivity<ActivitySetupBinding>(R.layout.activity_setup) {

    override val onNavigateUp: ((BackNavigation) -> Unit)
        get() = {
            if (!findNavController(binding.navHostFragment.id).popBackStack()) finish()
        }

    override val statusBarTransparent = true
}
