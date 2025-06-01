package fr.epf.min1.projet_android_de_chassey_flouvat.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.epf.min1.projet_android_de_chassey_flouvat.services.AuthService
import fr.epf.min1.projet_android_de_chassey_flouvat.services.LoginRequest
import fr.epf.min1.projet_android_de_chassey_flouvat.services.LoginResponse
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class AuthRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://fakestoreapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val authService = retrofit.create(AuthService::class.java)

    fun login(username: String, password: String): LiveData<String?> {
        val result = MutableLiveData<String?>()

        authService.login(LoginRequest(username, password)).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    result.value = response.body()?.token
                } else {
                    Log.e("AUTH", "Échec login : ${response.code()}")
                    result.value = null
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("AUTH", "Erreur login", t)
                result.value = null
            }
        })

        return result
    }
}