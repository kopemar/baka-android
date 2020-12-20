package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.url

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.afollestad.vvalidator.form
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

        form {
            input(binding.etUsername.id) {
                assert(getString(R.string.username_must_be_filled)) {
                    it.text.isNotBlank()
                }
            }

            input(binding.etPassword.id) {
                assert(getString(R.string.password_must_be_filled)) {
                    it.text.isNotBlank()
                }
            }

            submitWith(binding.btnLogIn.id) {
                activity?.hideKeyboard()
                viewModel.signIn()
            }
        }
    }
}