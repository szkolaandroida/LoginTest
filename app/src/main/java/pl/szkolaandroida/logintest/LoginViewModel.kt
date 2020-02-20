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

        if (username.value!!.isEmpty()) {
            usernameError.value = "username can't be empty"
        }
        if (password.value!!.length < 4) {
            passwordError.value = stringProvider.getString(R.string.password_cant_be_too_short)
        }

        if (usernameError.value!!.isEmpty() && passwordError.value!!.isEmpty()) {

            viewModelScope.launch {
                val response =
                    loginApi.getLogin(username = username.value!!, password = password.value!!)
                if (response.isSuccessful) {
                    loginNavigator.goToMainScreen()
                }
            }
        }

    }
}
