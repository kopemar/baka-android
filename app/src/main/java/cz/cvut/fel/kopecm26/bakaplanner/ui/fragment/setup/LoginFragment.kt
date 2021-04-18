package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.setup

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.afollestad.vvalidator.form
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentLoginBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hideKeyboard
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.LoginViewModel

class LoginFragment : ViewModelFragment<LoginViewModel, FragmentLoginBinding>(
    R.layout.fragment_login,
    LoginViewModel::class
) {

    private val signedInObserver = Observer<Boolean> {
        if (it) {
            findNavController().navigate(LoginFragmentDirections.openMainActivity())

            activity?.finish()
        }
    }

    override fun initUi() {
        viewModel.signedIn.observe(this, signedInObserver)

        binding.form.setOnFocusChangeListener { _, hasFocus ->
            Logger.d("cl root click")
            if (hasFocus) {
                requireActivity().hideKeyboard()
            }
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.navigateToNewOrganization())
        }

        form {
            inputLayout(binding.inputUsername.id) {
                this.isNotEmpty().description(R.string.field_must_be_filled)
            }

            inputLayout(binding.inputPassword.id) {
                this.isNotEmpty().description(R.string.field_must_be_filled)
            }

            submitWith(binding.btnLogIn.id) {
                activity?.hideKeyboard()
                viewModel.signIn()
            }
        }
    }
}
