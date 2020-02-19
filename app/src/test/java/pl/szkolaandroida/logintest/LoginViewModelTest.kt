package pl.szkolaandroida.logintest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody
import org.junit.Rule
import org.junit.Test
import retrofit2.Converter
import retrofit2.Response

class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainRule = MainCoroutineRule()


    private val loginApi = mock<LoginApi>()
    private val converter = mock<Converter<ResponseBody, ApiError>>()
    private val stringProvider = mock<StringProvider>()
    private val loginNavigator = mock<LoginNavigator>()

    val viewModel = LoginViewModel(loginApi, converter, stringProvider)

    @Test
    fun `show error on empty username`() {
        val error = "username empty error"
        whenever(stringProvider.getString(R.string.username_cant_be_empty)).thenReturn(error)
        viewModel.loginClicked()
        assertThat(viewModel.usernameError.value).isEqualTo(error)
    }

    @Test
    fun `show error when password is too short`() {
        //given
        val error = "password too short error"
        whenever(stringProvider.getString(any())).thenReturn("")
        whenever(stringProvider.getString(R.string.password_cant_be_too_short)).thenReturn(error)
        //when
        viewModel.password.value = "a"
        viewModel.loginClicked()
        //then
        assertThat(viewModel.passwordError.value).isEqualTo(error)
    }

    @Test
    fun `go to main screen after login`() = runBlockingTest {

        viewModel.loginNavigator = loginNavigator
        val username = "test"
        val password = "pass"
        viewModel.username.value = username
        viewModel.password.value = password

        whenever(loginApi.getLogin(username, password)).thenReturn(Response.success(
            LoginResponse(username, "id", "token")
        ))
        viewModel.loginClicked()
        verify(loginNavigator).goToMainScreen()
    }
}
