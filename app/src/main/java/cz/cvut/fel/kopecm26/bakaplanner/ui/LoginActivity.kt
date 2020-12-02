package cz.cvut.fel.kopecm26.bakaplanner.ui

import com.afollestad.vvalidator.form
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityLoginBinding
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity: ViewModelActivity<LoginViewModel, ActivityLoginBinding>(R.layout.activity_login, LoginViewModel::class) {

    override val statusBarTransparent = true

    override fun initUi() {
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
                GlobalScope.launch(Dispatchers.Default) {
                    viewModel.signIn()
                }
            }
        }
    }
}