package fr.epf.min1.projet_android_de_chassey_flouvat.services

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String
)

interface AuthService {
    @POST("auth/login")
    fun login(@Body credentials: LoginRequest): Call<LoginResponse>
}