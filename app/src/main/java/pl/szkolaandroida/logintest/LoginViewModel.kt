package pl.szkolaandroida.logintest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    }
}
