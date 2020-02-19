package pl.szkolaandroida.logintest

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

const val REST_APP_ID = "RRQfzogXeuQI2VzK0bqEgn02IElfm3ifCUf1lNQX"
const val REST_API_KEY = "mt4btJUcnmVaEJGzncHqkogm0lDM3n2185UNSjiX"
const val BASE_URL = "https://parseapi.back4app.com/"


interface LoginApi {

    @Headers(
        "X-Parse-Revocable-Session: 1",
        "X-Parse-Application-Id: $REST_APP_ID",
        "X-Parse-REST-API-Key: $REST_API_KEY"
    )
    @GET("login")
    suspend fun getLogin(
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<LoginResponse>
}

data class LoginResponse(
    val username: String,
    val objectId: String,
    val sessionToken: String
)

data class ApiError(
    val code: Int,
    val error: String
)

