package pl.szkolaandroida.logintest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
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

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val loginApi = mock<LoginApi>()
    private val converter = mock<Converter<ResponseBody, ApiError>>()
    private val stringProvider = mock<StringProvider>()
    private val loginNavigator = mock<LoginNavigator>()

    private val viewModel = LoginViewModel(loginApi, converter, stringProvider)

    @Test
    fun `show error when username is empty`() {
        //given
        val error = "username can't be empty"
        whenever(stringProvider.getString(R.string.username_cant_be_empty)).thenReturn(error)

        //when
        viewModel.loginClicked()

        //then
        assertThat(viewModel.usernameError.value).isEqualTo(error)
    }

    @Test
    fun `show error when password is too short`() {
        //given
        val error = "password is too short"
        whenever(stringProvider.getString(R.string.password_cant_be_too_short)).thenReturn(error)
        //when
        viewModel.username.value = "test" //valid username
        viewModel.password.value = "x" //password too short
        viewModel.loginClicked()
        //then
        assertThat(viewModel.passwordError.value).isEqualTo(error)
    }

    @Test
    fun `show main screen after login`() = runBlockingTest {
        // given
        val username = "test"
        val password = "pass"
        viewModel.loginNavigator = loginNavigator
        whenever(loginApi.getLogin(username, password)).thenReturn(Response.success(
            LoginResponse("test", "id", "token")
        ))
        // when
        viewModel.username.value = username
        viewModel.password.value = password
        viewModel.loginClicked()
        // then
        verify(loginNavigator).goToMainScreen()
    }
}
