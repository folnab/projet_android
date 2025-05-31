package fr.epf.min1.projet_android_de_chassey_flouvat.services

import fr.epf.min1.projet_android_de_chassey_flouvat.data.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    @GET("products")
    fun getAllProducts(): Call<List<Product>>

    @GET("products/{id}")
    fun getProductById(@Path("id") id: Int): Call<Product>
}
