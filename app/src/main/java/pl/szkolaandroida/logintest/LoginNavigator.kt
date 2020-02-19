package pl.szkolaandroida.logintest

import android.content.Intent

interface LoginNavigator {

    fun goToMainScreen()
}

class LoginActivityNavigator(private val loginActivity: LoginActivity) : LoginNavigator {

    override fun goToMainScreen() {
        loginActivity.startActivity(Intent(loginActivity, MainActivity::class.java))
        loginActivity.finish()
    }

}

