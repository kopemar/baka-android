package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.profile

import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentSettingsBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.BindingFragment

class SettingsFragment : BindingFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {
    override var navigateUp = true

    override val toolbar: Toolbar
        get() = binding.tToolbar.toolbar

    override fun initUi() {
        binding.btnUrl.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.navigateToUrl())
        }
    }
}
