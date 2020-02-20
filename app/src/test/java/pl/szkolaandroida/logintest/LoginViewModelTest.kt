package pl.szkolaandroida.logintest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class LoginViewModelTest {

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val stringProvider: StringProvider = mock()
    private val loginNavigator: LoginNavigator = mock()

    private val loginApi: LoginApi = mock()

    val viewModel = LoginViewModel(loginApi, mock(), stringProvider)

    @Test
    fun `show error when username is empty`() {

        //given
        val error = "username can't be empty"

        //when

        viewModel.loginClicked()

        //then

        assertThat(viewModel.usernameError.value).isEqualTo(error)
    }

    @Test
    fun `show error is password is too short`() {
        //given
        val error = "password is too short"
        whenever(stringProvider.getString(R.string.password_cant_be_too_short)).thenReturn(error)
        //when
        viewModel.password.value = "x"
        viewModel.loginClicked()
        //then
        assertThat(viewModel.passwordError.value).isEqualTo(error)
    }

    @Test
    fun `show main screen after login`() = runBlockingTest {
        //given
        val username = "test"
        val password = "pass"
        viewModel.loginNavigator = loginNavigator
        whenever(loginApi.getLogin(username, password)).thenReturn(Response.success(
            LoginResponse("test", "id", "token")
        ))


        //
        viewModel.username.value = username
        viewModel.password.value = password

        viewModel.loginClicked()

        //
        verify(loginNavigator).goToMainScreen()

    }
}