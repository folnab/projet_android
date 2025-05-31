package fr.epf.min1.projet_android_de_chassey_flouvat.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.epf.min1.projet_android_de_chassey_flouvat.data.Product
import fr.epf.min1.projet_android_de_chassey_flouvat.services.ProductService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://fakestoreapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(ProductService::class.java)

    fun getAllProducts(): LiveData<List<Product>> {
        val data = MutableLiveData<List<Product>>()

        service.getAllProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    data.value = response.body()
                    Log.d("API_RESPONSE", "Produits récupérés : ${response.body()}")
                }

            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.e("API_ERROR", "Erreur API : ${t.message}")
                data.value = emptyList()
            }
        })

        return data
    }

    fun getProductById(id: Int): LiveData<Product?> {
        val data = MutableLiveData<Product?>()

        service.getProductById(id).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                data.value = null
            }
        })

        return data
    }
}
