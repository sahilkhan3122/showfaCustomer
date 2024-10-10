package ui.welcome

import android.content.Intent
import android.os.Bundle
import base.BaseActivity
import com.example.showfa_customer_android.BR
import com.example.showfa_customer_android.R
import com.example.showfa_customer_android.databinding.ActivityWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint
import ui.login.LoginActivity
import ui.register.RegisterActivity

@AndroidEntryPoint
class WelcomeActivity() : BaseActivity<ActivityWelcomeBinding, WelComeViewModel>(),
    WelcomeNavigator {
    override val layoutId: Int
        get() = R.layout.activity_welcome
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setupObservable() {
        mViewModel.setNavigator(this)
    }

    override fun onLoginButtonClick() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onRegisterButtonClick() {
        startActivity(Intent(this, RegisterActivity::class.java))

    }

    }