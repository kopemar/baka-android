package cz.cvut.fel.kopecm26.bakaplanner.ui

import com.afollestad.vvalidator.form
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityLoginBinding
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity: ViewModelActivity<LoginViewModel, ActivityLoginBinding>(R.layout.activity_login, LoginViewModel::class) {
    override fun initUi() {
        form {
            submitWith(binding.btnLogIn.id) {
                GlobalScope.launch(Dispatchers.Default) {
                    viewModel.signIn()
                }
            }
        }
    }
}