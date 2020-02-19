package pl.szkolaandroida.logintest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import pl.szkolaandroida.logintest.databinding.LoginActivityBinding

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModel()
    private val loginNavigator: LoginNavigator by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<LoginActivityBinding>(
            this, R.layout.login_activity
        ).apply {
            this.vm = viewModel
            lifecycleOwner = this@LoginActivity
        }
    }
}

class LoginActivityNavigator(private val loginActivity: LoginActivity) : LoginNavigator {

    override fun goToMainScreen() {
        loginActivity.startActivity(Intent(loginActivity, MainActivity::class.java))
        loginActivity.finish()
    }

}


