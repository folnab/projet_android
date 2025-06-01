package fr.epf.min1.projet_android_de_chassey_flouvat.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("username") val username: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("password") val password: String = ""
)