package pl.szkolaandroida.logintest

import android.app.Application
import android.content.Context
import androidx.annotation.StringRes
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}

val appModule = module {
    viewModel {
        LoginViewModel(
            loginApi = get(),
            errorConverter = get(),
            stringProvider = get()
        )
    }

    single<Retrofit> {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(client)
            .build()
        return@single retrofit
    }

    single<LoginApi> {
        get<Retrofit>().create(LoginApi::class.java)
    }

    single<Converter<ResponseBody, ApiError>> {
        get<Retrofit>().responseBodyConverter(ApiError::class.java, emptyArray())
    }

    factory {
        AndroidStringProvider(androidContext())
    } bind StringProvider::class

    factory { (loginActivity: LoginActivity) ->
        LoginActivityNavigator(loginActivity)
    } bind LoginNavigator::class

}