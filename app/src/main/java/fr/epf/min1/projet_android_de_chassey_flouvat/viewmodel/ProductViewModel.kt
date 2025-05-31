package fr.epf.min1.projet_android_de_chassey_flouvat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import fr.epf.min1.projet_android_de_chassey_flouvat.data.Product
import fr.epf.min1.projet_android_de_chassey_flouvat.repository.ProductRepository

class ProductViewModel : ViewModel() {

    private val repository = ProductRepository()

    val products: LiveData<List<Product>> by lazy {
        repository.getAllProducts()
    }

    fun getProduct(id: Int): LiveData<Product?> {
        return repository.getProductById(id)
    }

    // autre implémentation pour la suite
}
