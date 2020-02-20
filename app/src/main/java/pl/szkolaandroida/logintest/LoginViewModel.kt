package pl.szkolaandroida.logintest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Converter

class LoginViewModel(
    private val loginApi: LoginApi,
    private val errorConverter: Converter<ResponseBody, ApiError>,
    private val stringProvider: StringProvider
) : ViewModel() {

    lateinit var loginNavigator: LoginNavigator

    val username = MutableLiveData<String>("")
    val usernameError = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    val passwordError = MutableLiveData<String>("")

    fun loginClicked() {
        usernameError.value = ""
        passwordError.value = ""

        val username = username.value!!
        val password = password.value!!

        if (username.isEmpty()) {
            usernameError.value = stringProvider.getString(R.string.username_cant_be_empty)
        }
        if (password.length < 4) {
            passwordError.value = stringProvider.getString(R.string.password_cant_be_too_short)
        }

        if (usernameError.value!!.isEmpty() && passwordError.value!!.isEmpty()) {
            performLogin(username, password)
        }
    }

    private fun performLogin(username: String, password: String) {
        viewModelScope.launch {
            val response = loginApi.getLogin(username, password)
            if (response.isSuccessful) {
                loginNavigator.goToMainScreen()
            } else {
                response.errorBody()?.let {
                    val apiError: ApiError? = errorConverter.convert(it)
                    passwordError.value = apiError?.error
                }
            }
        }
    }
}
