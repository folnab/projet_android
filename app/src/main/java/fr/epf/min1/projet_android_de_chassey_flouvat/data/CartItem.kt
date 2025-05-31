package fr.epf.min1.projet_android_de_chassey_flouvat.data

data class CartItem(
    val product: Product,
    var quantity: Int
) {
    val totalPrice: Double
        get() = product.price * quantity
}