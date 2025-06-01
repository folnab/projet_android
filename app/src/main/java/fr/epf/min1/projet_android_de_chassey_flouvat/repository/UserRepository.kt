package fr.epf.min1.projet_android_de_chassey_flouvat.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.epf.min1.projet_android_de_chassey_flouvat.data.Product
import fr.epf.min1.projet_android_de_chassey_flouvat.data.User
import fr.epf.min1.projet_android_de_chassey_flouvat.services.UserService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    val retrofit = Retrofit.Builder()
        .baseUrl("https://fakestoreapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userService = retrofit.create(UserService::class.java)

    fun getAllUsers(): LiveData<List<User>> {
        val data = MutableLiveData<List<User>>()

        userService.getAllUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    data.value = response.body()
                    Log.d("API_RESPONSE", "Utilisateurs récupérés : ${response.body()}")
                }

            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("API_ERROR", "Erreur API : ${t.message}")
                data.value = emptyList()
            }
        })

        return data
    }

    fun getUserById(id: Int): LiveData<User?> {
        val data = MutableLiveData<User?>()

        userService.getUserById(id).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                data.value = null
            }
        })

        return data
    }

    fun getUserByUsername(username: String): LiveData<User?> {
        val data = MutableLiveData<User?>()

        getAllUsers().observeForever { users ->
            val user = users.find { it.username == username }
            data.value = user
        }

        return data
    }

    fun createUser(user: User): LiveData<User?> {
        val data = MutableLiveData<User?>()

        userService.createNewUser(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                data.value = null
            }
        })

        return data
    }
}